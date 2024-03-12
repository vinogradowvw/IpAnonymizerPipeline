package ipanonymizerpipeline;


import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteBufferDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;



public class Kafka extends Thread {

    private final KafkaConsumer<byte[], ByteBuffer> сonsumer;
    public PreparedStatement insertStatement;

    Kafka (String bootstrapServises, String kafkaTopic, String groupId) {
        Properties props = new Properties();

        props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServises);
        props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteBufferDeserializer.class.getName());

        this.сonsumer = new KafkaConsumer<>(props);
        this.сonsumer.subscribe(Collections.singletonList(kafkaTopic));
        System.err.println("Connected to kafka");
    }

    private void consumeMessages () {

        while (true) {

            ConsumerRecords<byte[], ByteBuffer> records = this.сonsumer.poll(Duration.ofMillis(100));
        
            for (ConsumerRecord<byte[], ByteBuffer> record : records) {
                try {
                    HtmlLog.HttpLogRecord.Reader httpLog = CapnpDeserializer.getDeserializer(record.value());
                    System.out.println(Anonymizer.anonymizeIp(httpLog.getRemoteAddr().toString()));
                    addLogToSQLStatement(insertStatement, httpLog);
                } catch (Exception e) {
                    System.out.println("Cannot deserialize message" + e);
                }
            }
            this.сonsumer.commitAsync();
        }
    }

    private void addLogToSQLStatement (PreparedStatement insertStatement, HtmlLog.HttpLogRecord.Reader httpLog) {
        try {
            insertStatement.setTimestamp(1, new Timestamp(httpLog.getTimestampEpochMilli()));
            insertStatement.setLong(2,  httpLog.getResourceId());
            insertStatement.setLong(3, httpLog.getBytesSent());
            insertStatement.setLong(4, httpLog.getRequestTimeMilli());
            insertStatement.setShort(5, (short) httpLog.getResponseStatus());
            insertStatement.setString(6, httpLog.getCacheStatus().toString());
            insertStatement.setString(7, httpLog.getMethod().toString());
            insertStatement.setString(8, Anonymizer.anonymizeIp(httpLog.getRemoteAddr().toString()));
            insertStatement.setString(9, httpLog.getUrl().toString());
            insertStatement.addBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        System.out.println("Starting consuming");
        this.consumeMessages();
    }
}

