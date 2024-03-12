package ipanonymizerpipeline;

import java.util.Properties;

public class Main {
    public static void main (String[] args) {

        Properties props = new Properties();

        Kafka consumer = new Kafka("localhost:9092", "http_log", "data-engineering-task-reader");
        ClickHouseClient clickHouseClient = new ClickHouseClient("jdbc:clickhouse://localhost:8124/default", consumer, props);

        clickHouseClient.start();
    }
}
