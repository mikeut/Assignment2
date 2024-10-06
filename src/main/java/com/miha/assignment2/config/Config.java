package com.miha.assignment2.config;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * This class is an example of a configuration class.
 */
@Configuration
public class Config {
    @Bean
    public DataSource dataSource() {
        //create a data source
        return null;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public KafkaProducer<String, String> kafkaProducer() {
        //create a Kafka producer
        return new KafkaProducer<>(null);
    }
}
