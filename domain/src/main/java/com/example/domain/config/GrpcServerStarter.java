package com.example.domain.config;

import com.example.domain.domain.Order;
import com.example.domain.repository.OrderRepository;
import io.grpc.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class GrpcServerStarter implements CommandLineRunner {
    private final Server grpcServer;
    private final OrderRepository orderRepository;

    @Override
    public void run(String... args) throws Exception {
        var order = new Order()
                .setOrderNumber("ORD-001")
                .setOrderDate(LocalDateTime.now())
                .setClientName("John Doe")
                .setTotalAmount(new BigDecimal("100.00"));
        orderRepository.save(order);

        grpcServer.start();
        grpcServer.awaitTermination();
    }
}