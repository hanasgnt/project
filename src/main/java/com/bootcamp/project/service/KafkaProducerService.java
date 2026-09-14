package com.bootcamp.project.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.bootcamp.project.dto.TransactionEvent;

@Service
public class KafkaProducerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);

    private static final String TOPIC = "transactions";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    // public void sendTransaction(Object transaction) {
    // logger.info("Sending transaction to Kafka topic: {}", TOPIC);
    // kafkaTemplate.send(TOPIC, transaction);
    // logger.info("Transaction sent successfully to Kafka topic: {}", TOPIC);
    // }

    public void sendTransaction(TransactionEvent event) {
        logger.info("Sending transaction to Kafka topic: {}", TOPIC);
        kafkaTemplate.send(TOPIC, event).whenComplete((result, exception) -> {
            if (exception != null) {
                logger.error("Failed to send transaction to Kafka", exception);
                return;
            }

            logger.info(
                    "Transaction sent successfully to Kafka topic: {}, partition: {}, offset: {}",
                    TOPIC,
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset());
        });
    }
}