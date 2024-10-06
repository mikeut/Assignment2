package com.miha.assignment2.service;

import com.miha.assignment2.consumer.MyConsumerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class is an example of a service class that consumes messages from Kafka. It uses an executor service to create
 * multiple consumers that consume messages from multiple Kafka partitions.
 */
public class KafkaConsumerService {

    @Value("${kafka.partitions}")
    private Integer partitionCount;

    private final ExecutorService executorService;
    private final List<Callable<Void>> tasks = new ArrayList<>();

    public KafkaConsumerService(MyConsumerFactory consumerFactory) {
        this.executorService = Executors.newFixedThreadPool(partitionCount);
        for (int i = 0; i <= partitionCount; i++) {
            tasks.add(consumerFactory.createConsumer());
        }
    }

    public void startConsuming() throws InterruptedException {
        executorService.invokeAll(tasks);
    }
}
