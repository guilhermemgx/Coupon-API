package br.com.couponapi.domain;

import lombok.Getter;

@Getter
public class CouponCode {

    private String value;

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
