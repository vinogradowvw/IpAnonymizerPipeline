package ipanonymizerpipeline;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.logging.Logger;

import com.clickhouse.jdbc.ClickHouseDataSource;

import java.sql.Connection;


public class ClickHouseClient implements Runnable {

    public Connection connection;
    private final Kafka consumer;
    public PreparedStatement insertStatement;
    private Logger logger = Logger.getLogger(ClickHouseClient.class.getName());


    ClickHouseClient(String url, Kafka consumer, Properties props) {

        try {
            ClickHouseDataSource dataSource = new ClickHouseDataSource(url, props);
            this.connection = dataSource.getConnection();
            logger.info("Connected to ClickHouse");
        } catch (Exception e) {
            logger.warning("Can not connect to ClickHouse, stopping executing");
            e.printStackTrace();
            System. exit(0);
        };

        this.consumer = consumer;
        try {
            this.prepareInsertStatement();
        } catch (SQLException e) {
            e.printStackTrace();
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
                preparedStatement.executeBatch();
                logger.info("Inserted http logs at " + LocalDateTime.now());
                return;
            } catch (Exception e) {
                logger.warning("Failed inserting logs, attempt " + attempt);
                e.printStackTrace();
                Thread.sleep(2500);
            }
        }
        logger.warning("Failed inserting logs after " + 3 + " attempts, stopping execution");
        System.exit(0);
    }

    @Override
    public void run() {
        logger.info("Starting posting");
        synchronized (this.insertStatement) {
            this.consumer.isConsuming = true;
            this.insertStatement.notify();
        }
        
        while (true) {
            try {
                Thread.sleep(60000);
                this.consumer.isConsuming = false;
                logger.info("Stropping Kafka");
                synchronized (this.insertStatement) {    
                    this.executeQuery(this.consumer.insertStatement);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                synchronized (this.insertStatement) {
                    this.consumer.isConsuming = true;
                    this.insertStatement.notify();
                    logger.info("Starting Kafka");
                    }
                }
            }
    }
}
