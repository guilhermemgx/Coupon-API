package br.com.couponapi.repository.jpa;

import br.com.couponapi.domain.Coupon;
import br.com.couponapi.domain.CouponCode;
import br.com.couponapi.domain.DiscountValue;
import org.springframework.stereotype.Component;

@Component
public class CouponEntityMapper {

    public CouponEntity toEntity(Coupon coupon) {
        return new CouponEntity(
                coupon.getId(),
                coupon.getCode().getValue(),
                coupon.getDescription(),
                coupon.getDiscountValue().getValue(),
                coupon.getExpirationDate(),
                coupon.getStatus(),
                coupon.isPublished(),
                coupon.isRedeemed(),
                coupon.getDeletedAt()
        );
    }

    public Coupon toDomain(CouponEntity entity) {
        return new Coupon(
                entity.getId(),
                new CouponCode(entity.getCode()),
                entity.getDescription(),
                new DiscountValue(entity.getDiscountValue()),
                entity.getExpirationDate(),
                entity.getStatus(),
                entity.isPublished(),
                entity.isRedeemed(),
                entity.getDeletedAt()
        );
    }
}
