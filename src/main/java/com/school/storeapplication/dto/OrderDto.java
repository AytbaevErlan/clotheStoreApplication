package com.school.storeapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderDto {
    private Long id;
    private BigDecimal total;
    private Instant createdAt;
    private String status;
    private List<OrderItemDto> items;
}
