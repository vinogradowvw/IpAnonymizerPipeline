package ipanonymizerpipeline;

import java.util.Properties;

public class Main {
    public static void main (String[] args) {

        Properties props = new Properties();

        Kafka consumer = new Kafka("localhost:9092", "http_log", "data-engineering-task-reader");
        
        ClickHouseClient chClient = new ClickHouseClient("jdbc:clickhouse:http://localhost:8124/default", consumer, props);

        Thread chThread = new Thread(chClient, "chThread");
        chThread.start();
        Thread kThread = new Thread(consumer, "kThread");
        kThread.start();
    }
}
