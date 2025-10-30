package com.dgramada.order.dto;

import com.dgramada.order.model.OrderStatus;
import com.dgramada.product.dto.ProductResponse;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        OrderStatus status,
        LocalDateTime createdAt,
        String customerName,
        String customerEmail,
        List<ProductResponse> products
) {
}
