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

create table http_log (
  timestamp DateTime,
  resource_id UInt64,
  bytes_sent UInt64,
  request_time_milli UInt64,
  response_status UInt16,
  cache_status LowCardinality(String),
  method LowCardinality(String),
  remote_addr String,
  url String) ENGINE = Log