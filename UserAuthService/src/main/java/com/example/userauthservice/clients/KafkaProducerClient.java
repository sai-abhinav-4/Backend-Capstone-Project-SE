package com.example.userauthservice.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducerClient {

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topic, String message) {
        // Simulate sending a message to Kafka
        System.out.println("Sending message to topic " + topic + ": " + message);
        kafkaTemplate.send(topic, message);
    }
}
