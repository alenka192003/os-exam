package com.example.gateway.rest;

import order.OrderOuterClass;
import order.OrderServiceGrpc;
import com.example.gateway.redis.RedisOperations;
import com.example.gateway.rest.dto.OrderDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/orders")
public class OrderController {
    @Value("${grpc.server.host}")
    private String grpcHost;

    @Value("${grpc.server.port}")
    private int grpcPort;

    public static final String REDIS_KEY_ORDERS_ALL = "orders::all";

    private ManagedChannel channel;
    private OrderServiceGrpc.OrderServiceBlockingStub stub;

    private final ObjectMapper objectMapper;
    private final RabbitTemplate rabbitTemplate;
    private final RedisOperations<OrderDto> redisOperations;

    @PostConstruct
    private void init() {
        this.channel = ManagedChannelBuilder.forAddress(grpcHost, grpcPort)
                .usePlaintext()
                .build();
        this.stub = OrderServiceGrpc.newBlockingStub(channel);
    }

    @GetMapping
    public List<OrderDto> getAllOrders() {
        log.info("GET request for getting all orders");
        List<OrderDto> orders;
        List<OrderDto> cachedOrders = redisOperations.get(REDIS_KEY_ORDERS_ALL);

        if (cachedOrders.isEmpty()) {
            OrderOuterClass.Empty request = OrderOuterClass.Empty.newBuilder().build();
            orders = stub.getAllOrders(request).getOrdersList().stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());
        } else {
            return cachedOrders;
        }
        redisOperations.save(REDIS_KEY_ORDERS_ALL, orders);

        return orders;
    }

    @GetMapping("/{id}")
    public OrderDto getOrder(@PathVariable String id) {
        log.info("GET request for getting order with (id: {})", id);
        OrderOuterClass.OrderRequest request = OrderOuterClass.OrderRequest.newBuilder()
                .setId(id)
                .build();
        return toDto(stub.getOrderById(request));
    }

    @PostMapping
    public void createOrder(@RequestBody OrderDto orderDto) {
        log.info("POST request for creating (order: {})", orderDto);
        try {
            String addOrderJson = objectMapper.writeValueAsString(orderDto);
            rabbitTemplate.convertAndSend("orderCreated", addOrderJson);
            redisOperations.delete(REDIS_KEY_ORDERS_ALL);
        } catch (JsonProcessingException e) {
            log.info("Error while writing JSON to string");
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/{id}")
    public void updateOrder(@PathVariable String id, @RequestBody OrderDto orderDto) {
        log.info("PUT request for updating order with (id: {})", id);
        try {
            orderDto.setId(id);
            String updateOrderJson = objectMapper.writeValueAsString(orderDto);
            rabbitTemplate.convertAndSend("orderUpdated", updateOrderJson);
            redisOperations.delete(REDIS_KEY_ORDERS_ALL);
        } catch (JsonProcessingException e) {
            log.info("Error while writing JSON to string");
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable String id) {
        log.info("DELETE request for deleting order with (id: {})", id);
        rabbitTemplate.convertAndSend("orderDeleted", id);
        redisOperations.delete(REDIS_KEY_ORDERS_ALL);
    }

    private OrderDto toDto(OrderOuterClass.Order order) {
        return new OrderDto()
                .setId(order.getId())
                .setOrderNumber(order.getOrderNumber())
                .setOrderDate(LocalDateTime.ofInstant(
                        Instant.ofEpochSecond(order.getOrderDate()),
                        ZoneId.systemDefault()))
                .setClientName(order.getClientName())
                .setTotalAmount(BigDecimal.valueOf(order.getTotalAmount()));
    }
}