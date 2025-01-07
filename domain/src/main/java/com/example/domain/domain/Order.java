package com.example.domain.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Document(collection = "orders")
@Accessors(chain = true)
public class Order {
    @Id
    private String id;
    private String orderNumber;
    private LocalDateTime orderDate;
    private String clientName;
    private BigDecimal totalAmount;
}