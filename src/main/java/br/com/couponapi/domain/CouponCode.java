package br.com.couponapi.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class CouponCode {

    private String value;

    protected CouponCode() {
    }

    public CouponCode(String rawCode) {
        if (rawCode == null || rawCode.isBlank()) {
            throw new IllegalArgumentException("coupon code is required");
        }

        String sanitizedCode = rawCode.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();

        if (sanitizedCode.length() != 6) {
            throw new IllegalArgumentException("coupon code must have exactly 6 alphanumeric characters");
        }

        this.value = sanitizedCode;
    }
}
