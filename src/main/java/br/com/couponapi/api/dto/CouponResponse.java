package br.com.couponapi.api.dto;

import br.com.couponapi.domain.Coupon;
import br.com.couponapi.domain.CouponStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record CouponResponse(
        UUID id,
        String code,
        String description,
        BigDecimal discountValue,
        OffsetDateTime expirationDate,
        CouponStatus status,
        boolean published,
        boolean redeemed
) {

    public static CouponResponse from(Coupon coupon) {
        return new CouponResponse(
                coupon.getId(),
                coupon.getCode().getValue(),
                coupon.getDescription(),
                coupon.getDiscountValue().getValue(),
                coupon.getExpirationDate(),
                coupon.getStatus(),
                coupon.isPublished(),
                coupon.isRedeemed()
        );
    }
}
