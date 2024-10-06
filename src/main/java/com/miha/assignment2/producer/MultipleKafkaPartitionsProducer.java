package com.miha.assignment2.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * This class is responsible for producing messages to multiple Kafka partitions.The current implementation reads a file
 * line by line and sends each line to a Kafka topic. This can easily be replaced with consumer from another Kafka/stream.
 * <p>
 * The partition is determined by the matchId. The matchId is extracted from the line and the key is calculated as
 * matchId % partitionCount. This way, the messages are distributed evenly across all partitions and matches with same
 * ids are always sent to the same partition. This ensures that the order of messages is preserved for the same match and on
 * the consumer side we can have many consumers running in parallel without worrying about the order of messages.
 */
public class MultipleKafkaPartitionsProducer {

    private final KafkaProducer<String, String> producer;

    @Value("${kafka.partitions}")
    private Integer partitionCount;

    public MultipleKafkaPartitionsProducer(KafkaProducer<String, String> producer) {
        this.producer = producer;
    }

    public void produce() {
        try (Stream<String> stream = Files.lines(Paths.get("data.txt"))) {
            stream.forEach(line -> {
                var matchIdString = line.split("\\|")[0];
                String matchId = matchIdString.replace("sr:match:", "");
                ProducerRecord<String, String> record = new ProducerRecord<>(
                    "topic",
                    Long.parseLong(matchId) % partitionCount + "", //using % to calculate the key which ensures same match ids are sent to the same partition
                    line);
                producer.send(record);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
