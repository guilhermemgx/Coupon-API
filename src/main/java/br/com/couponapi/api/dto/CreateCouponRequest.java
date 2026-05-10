package br.com.couponapi.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record CreateCouponRequest(
        @NotBlank String code,
        @NotBlank String description,
        @NotNull BigDecimal discountValue,
        @NotNull OffsetDateTime expirationDate,
        Boolean published
) {
}
