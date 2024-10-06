package com.miha.assignment2.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.Duration;
import java.util.concurrent.Callable;

/**
 * Kafka consumer that consumes messages from a Kafka topic. In this example, the consumer reads messages from a Kafka and
 * inserts them into a database using a JdbcTemplate.
 */
public class MyKafkaConsumer implements Callable<Void> {

    private final KafkaConsumer<String, String> consumer;
    private final JdbcTemplate jdbcTemplate;

    public MyKafkaConsumer(KafkaConsumer<String, String> consumer, JdbcTemplate jdbcTemplate) {
        this.consumer = consumer;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Void call() {
        while (true) {
            try {
                //currently no error handling is implemented, this should be added. To ensure that no messages are lost, the
                //consumer should commit the offset only after the message is successfully processed
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5));
                records.forEach(record -> {
                    var parts = record.value().split("\\|");
                    //this is a simple insert, this should be inserted in batches, also transactions should be used
                    jdbcTemplate.update(
                        "INSERT INTO match_outcome (match_id, market_id, outcome_id, specifiers) VALUES (?, ?, ?, ?)",
                        parts[0], parts[1], parts[2], parts[3]
                    );
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
