package br.com.couponapi.domain;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
public class Coupon {

    private UUID id;
    private CouponCode code;
    private String description;
    private DiscountValue discountValue;
    private OffsetDateTime expirationDate;
    private CouponStatus status;
    private boolean published;
    private boolean redeemed;
    private OffsetDateTime deletedAt;

    public Coupon(
            String code,
            String description,
            BigDecimal discountValue,
            OffsetDateTime expirationDate,
            boolean published,
            OffsetDateTime now
    ) {
        validateDescription(description);
        validateExpirationDate(expirationDate, now);

        this.id = UUID.randomUUID();
        this.code = new CouponCode(code);
        this.description = description;
        this.discountValue = new DiscountValue(discountValue);
        this.expirationDate = expirationDate;
        this.status = CouponStatus.ACTIVE;
        this.published = published;
        this.redeemed = false;
    }

    public Coupon(
            UUID id,
            CouponCode code,
            String description,
            DiscountValue discountValue,
            OffsetDateTime expirationDate,
            CouponStatus status,
            boolean published,
            boolean redeemed,
            OffsetDateTime deletedAt
    ) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.discountValue = discountValue;
        this.expirationDate = expirationDate;
        this.status = status;
        this.published = published;
        this.redeemed = redeemed;
        this.deletedAt = deletedAt;
    }

    public void delete(OffsetDateTime now) {
        if (this.status == CouponStatus.DELETED) {
            throw new IllegalStateException("coupon is already deleted");
        }

        this.status = CouponStatus.DELETED;
        this.deletedAt = now;
    }

    private void validateDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("description is required");
        }
    }

    private void validateExpirationDate(OffsetDateTime expirationDate, OffsetDateTime now) {
        if (expirationDate == null) {
            throw new IllegalArgumentException("expiration date is required");
        }

        if (expirationDate.isBefore(now)) {
            throw new IllegalArgumentException("expiration date cannot be in the past");
        }
    }
}
