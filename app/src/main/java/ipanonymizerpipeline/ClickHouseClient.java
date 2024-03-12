package ipanonymizerpipeline;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.Properties;

import com.clickhouse.jdbc.ClickHouseDataSource;

import java.sql.Connection;


public class ClickHouseClient extends Thread {

    private Connection connection;
    public PreparedStatement preparedInsert;
    private final Kafka consumer;

    ClickHouseClient(String url, Kafka consumer, Properties props) {
        this.consumer = consumer;
        try {
            ClickHouseDataSource dataSource = new ClickHouseDataSource(url, props);
            this.connection = dataSource.getConnection();
            System.out.println("Connected to ClickHouse");
        } catch (Exception e) {
            System.out.println("Can not connect to ClickHouse, stopping executing");
            System.out.println(e);
            System. exit(0);
        };
        prepareInsertStatement();
    }

    private void prepareInsertStatement () {
        try {
            this.preparedInsert = this.connection.prepareStatement(
                "insert into http_log select * from input('timestamp DateTime," + //
                                        "    resource_id UInt64," + //
                                        "    bytes_sent UInt64," + //
                                        "    request_time_milli UInt64," + //
                                        "    response_status UInt16," + //
                                        "    cache_status LowCardinality(String)," + //
                                        "    method LowCardinality(String)," + //
                                        "    remote_addr String," + //
                                        "    url String')"
                );
        } catch (Exception e) {
            System.out.println("Failed making prepared insert SQL Statement, stopping executing");
            System.out.println(e.getMessage());
            System. exit(0);
        };
    }

    public synchronized void insertHttpLog () throws InterruptedException {

        // Trying 3 times to push the data to the clickhouse, just in case.

        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                this.preparedInsert.executeBatch();
                System.out.println("Inserted http logs at " + LocalDateTime.now());
                prepareInsertStatement();
                return;
            } catch (Exception e) {
                System.out.println("Failed inserting logs, attempt " + attempt);
                System.out.println(e.getMessage());
                Thread.sleep(2500);
            }
        }
        System.out.println("Failed inserting logs after " + 3 + " attempts, stopping execution");
        System.exit(0);
    }

    @Override
    public void run() {
        System.err.println("Starting posting");
        this.consumer.insertStatement = this.preparedInsert;

        this.consumer.start();

        while (true) {
            try {
                Thread.sleep(60000);
                synchronized (this.consumer) {
                    System.out.println("Stropping Kafka");
                    this.consumer.wait();
                    insertHttpLog();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                synchronized (this.consumer) {
                    this.consumer.notify();
                }
                System.out.println("Starting Kafka");
            }
        }
    }
}