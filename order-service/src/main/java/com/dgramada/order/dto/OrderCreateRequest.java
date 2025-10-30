package com.dgramada.order.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderCreateRequest(
        @NotEmpty
        List<Long> productIds,
        @NotNull
        Long customerId
) {
}
