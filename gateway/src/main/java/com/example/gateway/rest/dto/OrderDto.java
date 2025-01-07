package com.example.gateway.rest.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class OrderDto {
    private String id;
    private String orderNumber;
    private LocalDateTime orderDate;
    private String clientName;
    private BigDecimal totalAmount;
}