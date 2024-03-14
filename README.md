# Anonymizer ETL Pipeline

### To begin with

I choose this tast because this, basically, this is type of tasks i would like to do prefessionally.

From the list of laguages was presented in the task:
  * Go
  * C/C++
  * Java
  * Rust

I chosed Java.

I satarted with jaust consuming messages from kafka, which was easy untill capnp. The problem was, that Cap'n Proto for Java really lack some documentation (or I can't search the browser), all information on how to use this thing I gained from people asking contributors on GitHub about how to use this or that...

But finally, I compiled the source java class for the capnp deserialization. Making the anonymizer was also not a big deal.

### The proxy problem
I was stuck at the point where I need to insert the data to the DB, which I need to communicate with through a proxy with limit of 1 request/min. I decided to make a 2 separate classes with Runnable interface, Kafka and ClickHouseClient. So these classes will be running in 2 Threads, and will be synchronized by Prepared Statement, the Kafka will inseert data there and CHClient will stop Kafka and execute it from it when needed (Every 1 min.), so at this pont we can't loose the data. My system uses dependeny injection when the Kafka object is passed to the CHClient. I do not like to use DI that much, but in this case it looks logic (you literally passing the data source to the destination). So this soluction could be grate from perspective of further developement, f.e. adding new classes for data source and ajusting the ClickHouseClient for new ones, such as RabbitMQ or even maybe other DB.

### Time
For the whole project I spent around 7-9h approximately. I spent most of the time with capnp and solving problem with proxy.

### Next steps
As a Next step for improvement could be crating ability to process the data with more streams. But at this point with current demand of traffic from the "http-log-kafka-producer" it is not really necessary. Also, as I said, the application could be slightly refactored to "DIY data processing framework" that coud be used to solve more problems than just this one, but it needs a context to start refatoring.

### Alternative solutions.
As altenative, I can implement it with Spark Streaming or Flink as a data processing tool, it could be less flexible than app written from scratch, but maybe it will take responsibilities such as parallel processing on itself. But i am not sure if it is possible to make a capnp decoding there.

### Testing and benchmarking