SELECT
    count(*) as logs_count,
    sum(bytes_sent) as total_bytes_sent,
    