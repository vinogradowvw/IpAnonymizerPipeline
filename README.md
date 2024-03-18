# Anonymizer ETL Pipeline

### Dependencies

```gradle
implementation 'com.google.guava:guava:31.1-jre'
implementation 'org.capnproto:runtime:0.1.16'
implementation 'org.apache.kafka:kafka-clients:3.7.0'
implementation 'com.clickhouse:clickhouse-jdbc:0.6.0:all'
implementation 'org.slf4j:slf4j-api:2.0.10'
implementation 'org.slf4j:slf4j-simple:2.0.10'
implementation 'org.apache.httpcomponents.core5:httpcore5:5.2.4'
```

### Language

I choose this task because this, basically, is the type of task I would like to do professionally.

From the list of languages was presented in the task:
  * Go
  * C/C++
  * Java
  * Rust

I chose Java.

I started with just consuming messages from kafka, which was easy until capnp. The problem was that Cap'n Proto for Java really lacks some documentation (or I can't search the browser), all information on how to use this thing I gained from people asking contributors on GitHub about how to use this or that...

But finally, I compiled the source java class for the capnp deserialization. Making the anonymizer was also not a big deal.

### The proxy problem
I was stuck at the point where I needed to insert the data to the DB, which I needed to communicate with through a proxy with a limit of 1 request/min. I decided to make 2 separate classes with Runnable interface, Kafka and ClickHouseClient. So these classes will be running in 2 Threads, and will be synchronized by Prepared Statement, the Kafka will insert data there and CHClient will stop Kafka and execute Prepared Statement when needed (Every 1 min.), so at this point we can't lose the data. My system uses dependency injection when the Kafka object is passed to the CHClient. I do not like to use DI that much, but in this case it looks logical (you are literally passing the data source to the destination). In case if the proxy or db is down, the application will try 3 times to insert data, after which it will be waiting for the user input to try again. The problem here was that even after executing the statement with exception, the statement is losing the data, so it is a blank input statement. I solved this problem by adding an array with the data for backup reasons if something goes wrong.

So this solution could be great from the perspective of further development, f.e. adding new classes for data source and adjusting the ClickHouseClient for new ones, such as RabbitMQ or even maybe other DB.

### SQL

For the "total_served_traffic" table i used MATERIALIZED view, since it giving high results for this type of tasks

```SQL
CREATE TABLE total_served_traffic (
    resource_id UInt64,
    total_bytes_sent UInt64,
    sum_request_time_milli UInt64,
    response_status UInt16,
    logs_count UInt64,
    cache_status LowCardinality(String),
    remote_addr String
) ENGINE = AggregatingMergeTree()
ORDER BY = (resource_id, response_status, cache_status, remote_addr)


CREATE MATERIALIZED VIEW total_served_traffic_mv TO total_served_traffic AS
SELECT
    resource_id,
    response_status,
    cache_status,
    remote_addr,
    COUNT(*) AS logs_count,
    SUM(bytes_sent) AS total_bytes_sent,
    SUM(request_time_milli) AS sum_request_time_milli
FROM http_log
GROUP BY
    resource_id, response_status, cache_status, remote_addr;
```

### Time
For the whole project I spent around 10h approximately. I spent most of the time solving problems with proxy and exception handling and stuck a little bit in the beginning with the capnp.

### Next steps
As a Next step for improvement could be creating the ability to process the data with more streams. But at this point with current demand of traffic from the "http-log-kafka-producer" it is not really necessary. Also, as I said, the application could be slightly refactored to "DIY data processing framework" that could be used to solve more problems than just this one, but it needs a context to start refactoring.

### Alternative solutions.
As an alternative, I can implement it with Spark Streaming or Flink as a data processing tool, it could be less flexible than an app written from scratch, but maybe it will take responsibilities such as parallel processing on itself. But I am not sure if it is possible to make a capnp decoding there.

### Testing and benchmarking

Estimate disk space for http_log table:
~ 148 bytes each message
= 148 * average incoming message rate * retention of the aggregated data

Querying data from totals table ~ 1-7 ms
Benchmark results:
![Screenshot 2024-03-14 124352](https://github.com/vinogradowvw/IpAnonymizerPipeline/assets/143388794/cf08b354-e0d3-441b-9527-1d9640dc9e6b)
