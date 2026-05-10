package br.com.couponapi.domain;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class DiscountValue {

    private BigDecimal value;

    public DiscountValue(BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("discount value is required");
        }

        if (value.compareTo(BigDecimal.valueOf(0.5)) < 0) {
            throw new IllegalArgumentException("discount value must be at least 0.5");
        }

        this.value = value;
    }
}
