package ipanonymizerpipeline;


import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteBufferDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;



public class Kafka implements Runnable{

    private final KafkaConsumer<byte[], ByteBuffer> сonsumer;
    public PreparedStatement insertStatement;
    public boolean isConsuming = false;
    Logger logger = Logger.getLogger(Kafka.class.getName());


    Kafka (String bootstrapServises, String kafkaTopic, String groupId) {
        Properties props = new Properties();

        props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServises);
        props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteBufferDeserializer.class.getName());

        this.сonsumer = new KafkaConsumer<>(props);
        this.сonsumer.subscribe(Collections.singletonList(kafkaTopic));
        logger.info("Connected to kafka");
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
            logger.warning("Error while pasting the data to the SQL statement");
            e.printStackTrace();
        }
    }

    
    @Override
    public void run() {
        logger.info("Kafka started consuming");

        int counter = 0;

        while (true) {
            synchronized (this.insertStatement) {
                while (!this.isConsuming) {
                    try {
                        this.insertStatement.wait();
                    } catch (InterruptedException e) {
                        logger.info("InterruptedException while wait() in kafka");
                        e.printStackTrace();
                    }
                }

                ConsumerRecords<byte[], ByteBuffer> records = this.сonsumer.poll(Duration.ofMillis(100));

                for (ConsumerRecord<byte[], ByteBuffer> record : records) {
                    try {
                        HtmlLog.HttpLogRecord.Reader httpLog = CapnpDeserializer.getDeserializer(record.value());
                        counter++;
                        logger.info("Recieved message N" + counter);
                        addLogToSQLStatement(this.insertStatement, httpLog);
                    } catch (Exception e) {
                        logger.warning("Can not deserialize the message");
                        e.printStackTrace();
                    }
                }
                this.сonsumer.commitAsync();
            }
        }
    }

}

