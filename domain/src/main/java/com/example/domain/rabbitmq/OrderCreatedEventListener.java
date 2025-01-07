package com.example.domain.rabbitmq;

import com.example.domain.domain.Order;
import com.example.domain.repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedEventListener {
    private final ObjectMapper objectMapper;
    private final OrderRepository orderRepository;

    @RabbitListener(queues = "orderCreated")
    public void listen(String message) {
        Order order;
        try {
            System.out.println(message);
            order = objectMapper.readValue(message, Order.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        orderRepository.save(order);
    }
}