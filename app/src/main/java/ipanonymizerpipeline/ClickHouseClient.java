package ipanonymizerpipeline;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Properties;

import com.clickhouse.jdbc.ClickHouseDataSource;

import java.sql.Connection;


public class ClickHouseClient implements Runnable {

    public Connection connection;
    private final Kafka consumer;
    public PreparedStatement insertStatement;


    ClickHouseClient(String url, Kafka consumer, Properties props) {

        try {
            ClickHouseDataSource dataSource = new ClickHouseDataSource(url, props);
            this.connection = dataSource.getConnection();
            System.out.println("Connected to ClickHouse");
        } catch (Exception e) {
            System.out.println("Can not connect to ClickHouse, stopping executing");
            System.out.println(e);
            System. exit(0);
        };

        this.consumer = consumer;
        try {
            this.prepareInsertStatement();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    private synchronized void prepareInsertStatement () throws SQLException {
            PreparedStatement preparedStatement = this.connection.prepareStatement(
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
            this.insertStatement = preparedStatement;
            this.consumer.insertStatement = this.insertStatement;
    }

    public synchronized void executeQuery (PreparedStatement preparedStatement) throws InterruptedException {

        // Trying 3 times to push the data to the clickhouse, just in case.

        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                connection.setAutoCommit(false);
                preparedStatement.executeBatch();
                connection.commit();
                System.out.println("Inserted http logs at " + LocalDateTime.now());
                connection.setAutoCommit(true);
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
        synchronized (this.insertStatement) {
            this.consumer.isConsuming = true;
            this.insertStatement.notify();
        }
        
        while (true) {
            try {
                Thread.sleep(60000);
                this.consumer.isConsuming = false;
                System.out.println("Stropping Kafka");
                synchronized (this.insertStatement) {    
                    this.executeQuery(this.consumer.insertStatement);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                synchronized (this.insertStatement) {
                    this.consumer.isConsuming = true;
                    this.insertStatement.notify();
                    System.out.println("Starting Kafka");
                    }
                }
            }
    }
}



// create table http_log (
//   timestamp DateTime,
//   resource_id UInt64,
//   bytes_sent UInt64,
//   request_time_milli UInt64,
//   response_status UInt16,
//   cache_status LowCardinality(String),
//   method LowCardinality(String),
//   remote_addr String,
//   url String) ENGINE = Log