package com.example.domain.grpc;

import com.example.domain.domain.Order;
import com.example.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import order.OrderServiceGrpc;
import order.OrderOuterClass;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
@RequiredArgsConstructor
public class OrderGrpcService extends OrderServiceGrpc.OrderServiceImplBase {
    private final OrderRepository orderRepository;

    @Override
    public void getAllOrders(OrderOuterClass.Empty request, io.grpc.stub.StreamObserver<OrderOuterClass.OrderList> responseObserver) {
        var orders = orderRepository.findAll().stream()
                .map(this::map)
                .toList();

        OrderOuterClass.OrderList orderList = OrderOuterClass.OrderList.newBuilder()
                .addAllOrders(orders)
                .build();

        responseObserver.onNext(orderList);
        responseObserver.onCompleted();
    }

    @Override
    public void getOrderById(OrderOuterClass.OrderRequest request, io.grpc.stub.StreamObserver<OrderOuterClass.Order> responseObserver) {
        var order = map(orderRepository.findById(request.getId()).orElse(new Order()));

        responseObserver.onNext(order);
        responseObserver.onCompleted();
    }

    private OrderOuterClass.Order map(Order order) {
        return OrderOuterClass.Order.newBuilder()
                .setId(order.getId() != null ? order.getId() : "")
                .setOrderNumber(order.getOrderNumber() != null ? order.getOrderNumber() : "")
                .setClientName(order.getClientName() != null ? order.getClientName() : "")
                .setOrderDate(order.getOrderDate() != null ?
                        order.getOrderDate().toEpochSecond(ZoneOffset.UTC) : 0)
                .setTotalAmount(order.getTotalAmount() != null ?
                        order.getTotalAmount().doubleValue() : 0.0)
                .build();
    }
}