package com.dgramada.product.dto;

import java.math.BigDecimal;

public record ProductResponse(
        String name,
        BigDecimal price
) {
}
