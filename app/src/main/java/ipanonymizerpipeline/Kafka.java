package ipanonymizerpipeline;


import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteBufferDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;


/*
 * Class for Kafka consumer.
 */

public class Kafka implements Runnable{

    private final KafkaConsumer<byte[], ByteBuffer> сonsumer;
    public ArrayList<HtmlLog.HttpLogRecord.Reader> messagesBackup;
    public PreparedStatement insertStatement;
    public boolean isConsuming = false;
    Logger logger = Logger.getLogger(Kafka.class.getName());

    
    /*
    * Initializing consumer itself and backup for messages
    */

    Kafka (String bootstrapServises, String kafkaTopic, String groupId) {

        this.messagesBackup = new ArrayList<HtmlLog.HttpLogRecord.Reader>();

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


    
    /*
    * Method for adding log to the statement from deserializer
    */
    
    public void addLogToSQLStatement(PreparedStatement ps, HtmlLog.HttpLogRecord.Reader httpLog) {
        try {
            ps.setTimestamp(1, new Timestamp(httpLog.getTimestampEpochMilli()));
            ps.setLong(2,  httpLog.getResourceId());
            ps.setLong(3, httpLog.getBytesSent());
            ps.setLong(4, httpLog.getRequestTimeMilli());
            ps.setShort(5, (short) httpLog.getResponseStatus());
            ps.setString(6, httpLog.getCacheStatus().toString());
            ps.setString(7, httpLog.getMethod().toString());
            ps.setString(8, Anonymizer.anonymizeIp(httpLog.getRemoteAddr().toString()));
            ps.setString(9, httpLog.getUrl().toString());
            ps.addBatch();
        } catch (SQLException e) {
            logger.warning("Error while pasting the data to the SQL statement");
            e.printStackTrace();
        }
    }

    
    /*
    * Infinitely consuming messages and adding each to the Prepared Statement 
    */
    
    private void consumeMessages () {
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
                        logger.info("Recieved message");
                        messagesBackup.add(httpLog);
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

    // Starting the thread.
    
    @Override
    public void run() {
        logger.info("Kafka started consuming");
        consumeMessages();
    }

}

