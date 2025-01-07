package com.example.domain.rabbitmq;

import com.example.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderDeletedEventListener {
    private final OrderRepository orderRepository;

    @RabbitListener(queues = "orderDeleted")
    public void listen(String message) {
        System.out.println(message);
        orderRepository.deleteById(message);
    }
}
