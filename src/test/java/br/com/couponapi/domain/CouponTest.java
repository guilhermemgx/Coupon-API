package br.com.couponapi.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CouponTest {

    private static final OffsetDateTime NOW = OffsetDateTime.parse("2026-05-08T10:00:00Z");

    @Test
    void shouldCreateCouponRemovingSpecialCharactersFromCode() {
        Coupon coupon = new Coupon(
                "ABC-123",
                "cupom de teste",
                BigDecimal.valueOf(0.8),
                NOW.plusDays(1),
                true,
                NOW
        );

        assertThat(coupon.getCode().getValue()).isEqualTo("ABC123");
        assertThat(coupon.getStatus()).isEqualTo(CouponStatus.ACTIVE);
        assertThat(coupon.isPublished()).isTrue();
        assertThat(coupon.isRedeemed()).isFalse();
    }

    @Test
    void shouldNormalizeCouponCodeToUppercase() {
        CouponCode couponCode = new CouponCode("ab-123c");

        assertThat(couponCode.getValue()).isEqualTo("AB123C");
    }

    @Test
    void shouldNotCreateCouponWithCodeDifferentThanSixCharactersAfterSanitization() {
        assertThatThrownBy(() -> new CouponCode("A-12"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("coupon code must have exactly 6 alphanumeric characters");
    }

    @Test
    void shouldNotCreateCouponWithBlankDescription() {
        assertThatThrownBy(() -> new Coupon(
                "ABC123",
                " ",
                BigDecimal.valueOf(0.8),
                NOW.plusDays(1),
                false,
                NOW
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("description is required");
    }

    @Test
    void shouldNotCreateCouponWithPastExpirationDate() {
        assertThatThrownBy(() -> new Coupon(
                "ABC123",
                "cupom de teste",
                BigDecimal.valueOf(0.8),
                NOW.minusDays(1),
                false,
                NOW
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("expiration date cannot be in the past");
    }

    @Test
    void shouldNotCreateCouponWithDiscountLessThanMinimum() {
        assertThatThrownBy(() -> new Coupon(
                "ABC123",
                "cupom de teste",
                BigDecimal.valueOf(0.4),
                NOW.plusDays(1),
                false,
                NOW
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("discount value must be at least 0.5");
    }

    @Test
    void shouldSoftDeleteCoupon() {
        Coupon coupon = new Coupon(
                "ABC123",
                "cupom de teste",
                BigDecimal.valueOf(0.8),
                NOW.plusDays(1),
                false,
                NOW
        );

        coupon.delete(NOW.plusHours(1));

        assertThat(coupon.getStatus()).isEqualTo(CouponStatus.DELETED);
        assertThat(coupon.getDeletedAt()).isEqualTo(NOW.plusHours(1));
    }

    @Test
    void shouldNotDeleteAlreadyDeletedCoupon() {
        Coupon coupon = new Coupon(
                "ABC123",
                "cupom de teste",
                BigDecimal.valueOf(0.8),
                NOW.plusDays(1),
                false,
                NOW
        );

        coupon.delete(NOW.plusHours(1));

        assertThatThrownBy(() -> coupon.delete(NOW.plusHours(2)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("coupon is already deleted");
    }
}
