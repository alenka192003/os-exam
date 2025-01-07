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
public class OrderUpdatedEventListener {
    private final ObjectMapper objectMapper;
    private final OrderRepository orderRepository;

    @RabbitListener(queues = "orderUpdated")
    public void listen(String message) {
        Order order;
        try {
            System.out.println(message);
            order = objectMapper.readValue(message, Order.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        var updatingOrder = orderRepository.findById(order.getId())
                .orElse(new Order());
        if (order.getOrderNumber() != null && !order.getOrderNumber().isEmpty())
            updatingOrder.setOrderNumber(order.getOrderNumber());
        if (order.getClientName() != null && !order.getClientName().isEmpty())
            updatingOrder.setClientName(order.getClientName());
        if (order.getOrderDate() != null)
            updatingOrder.setOrderDate(order.getOrderDate());
        if (order.getTotalAmount() != null)
            updatingOrder.setTotalAmount(order.getTotalAmount());
        orderRepository.save(updatingOrder);
    }
}