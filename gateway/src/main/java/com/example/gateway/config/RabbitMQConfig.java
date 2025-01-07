package com.example.gateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {
    @Value("${rabbitmq.queues.order-created}")
    private String orderCreatedQueueName;
    @Value("${rabbitmq.queues.order-updated}")
    private String orderUpdatedQueueName;
    @Value("${rabbitmq.queues.order-deleted}")
    private String orderDeletedQueueName;

    @Bean
    public Queue orderCreatedQueue() {
        return new Queue(orderCreatedQueueName, false);
    }

    @Bean
    public Queue orderUpdatedQueue() {
        return new Queue(orderUpdatedQueueName, false);
    }

    @Bean
    public Queue orderDeletedQueue() {
        return new Queue(orderDeletedQueueName, false);
    }
}