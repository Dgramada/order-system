package com.dgramada.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CustomerCreateRequest(
        @NotBlank
        String name,
        @Email
        @NotBlank
        String email
) {
}
