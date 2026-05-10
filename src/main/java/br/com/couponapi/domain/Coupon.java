package br.com.couponapi.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "coupons")
public class Coupon {

    @Id
    private UUID id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "code", nullable = false, length = 6))
    private CouponCode code;

    @Column(nullable = false, length = 500)
    private String description;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "discount_value", nullable = false))
    private DiscountValue discountValue;

    @Column(nullable = false)
    private OffsetDateTime expirationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponStatus status;

    @Column(nullable = false)
    private boolean published;

    @Column(nullable = false)
    private boolean redeemed;

    private OffsetDateTime deletedAt;

    protected Coupon() {
    }

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
