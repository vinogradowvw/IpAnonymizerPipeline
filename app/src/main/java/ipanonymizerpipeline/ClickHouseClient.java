package ipanonymizerpipeline;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Logger;

import com.clickhouse.jdbc.ClickHouseDataSource;

import java.sql.Connection;

/*
 * This class is responsible for connection with database and sending the data to it.
 */

public class ClickHouseClient implements Runnable {

    public Connection connection;
    private final Kafka consumer;
    public PreparedStatement insertStatement;
    private Logger logger = Logger.getLogger(ClickHouseClient.class.getName());

    /*
     * <init> getting connection to db by url and getting the object of Kafka to know what is the needed data source.
     */

    ClickHouseClient(String url, Kafka consumer, Properties props) {

        Scanner scanner = new Scanner(System.in);
        while (true){
            try {
                ClickHouseDataSource dataSource = new ClickHouseDataSource(url, props);
                this.connection = dataSource.getConnection();
                logger.info("Connected to ClickHouse");
                break;
            } catch (Exception e) {
                logger.warning("Can not connect to ClickHouse");
                e.printStackTrace();
                System.out.println("Try again? [y/n]");

                while (true) {
                    String input = scanner.nextLine();
                    if (input.equalsIgnoreCase("y")) {
                        System.out.println("Trying again...");
                        scanner.close();
                        break;
                    } else if (input.equalsIgnoreCase("n")) {
                        System.out.println("Exiting...");
                        scanner.close();
                        System.exit(0);
                    } else {
                        System.out.println("Invalid input. Please enter 'y' or 'n'.");
                    }
                }
            };
        }
        

        this.consumer = consumer;
        try {
            PreparedStatement preparedStatement = this.prepareInsertStatement();
            this.insertStatement = preparedStatement;
            this.consumer.insertStatement = this.insertStatement;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    /*
     * The method prepareInsertStatement returning needed blank Prepared Statement
     */
    
    private synchronized PreparedStatement prepareInsertStatement () throws SQLException {
            return(this.connection.prepareStatement(
                "insert into http_log select * from input('timestamp DateTime," + //
                                        "    resource_id UInt64," + //
                                        "    bytes_sent UInt64," + //
                                        "    request_time_milli UInt64," + //
                                        "    response_status UInt16," + //
                                        "    cache_status LowCardinality(String)," + //
                                        "    method LowCardinality(String)," + //
                                        "    remote_addr String," + //
                                        "    url String')"));
    }

    
    /*
     * tryExecute is method in which implemented logic with executing the statement and catching and handling the exceptions.
     */

    public synchronized void tryExecute() throws InterruptedException, SQLException {

        // Trying 3 times to push the data to the clickhouse, after waiting for user input
        
        
        int attemptCount = 0;

        while (true) {
            attemptCount++;
            try {
                this.insertStatement.executeBatch();
                logger.info("Data uploaded successfully.");
                this.consumer.messagesBackup = new ArrayList<HtmlLog.HttpLogRecord.Reader>();
                break;
            } catch (SQLException  e) {
                for (HtmlLog.HttpLogRecord.Reader httpLog : this.consumer.messagesBackup) {
                    this.consumer.addLogToSQLStatement(this.insertStatement, httpLog);
                }
                logger.warning("Failed to upload data: " + e.getMessage());
                Thread.sleep(2500);}

            if (attemptCount >= 3) {
                Scanner scanner = new Scanner(System.in);
                logger.warning("Connection to the database is invalid. Please check the database connection.");
                System.out.println("Press 'y' to try again");
                while (true) {
                    String input = scanner.nextLine();
                    if (input.equalsIgnoreCase("y")) {
                        System.out.println("Trying again...");
                        attemptCount = 0;
                        scanner.close();
                        break;
                    } else {
                        System.out.println("Invalid input. Please enter 'y' after fixing the connection");
                    }
                }
                scanner.close();
            }
        }
    }

    
    /*
     * The method for the running thread. Basically starting the whole pipeline from here. every 60,1 seconds stopping conusumer and sending the data to DB
     */

    @Override
    public void run() {
        logger.info("Starting posting");
        synchronized (this.insertStatement) {
            this.consumer.isConsuming = true;
            this.insertStatement.notify();
        }
        
        while (true) {
            try {
                Thread.sleep(60100);
                this.consumer.isConsuming = false;
                logger.info("Stropping Kafka");
                synchronized (this.insertStatement) {    
                    try {tryExecute();} catch (Exception e) {} ;
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
