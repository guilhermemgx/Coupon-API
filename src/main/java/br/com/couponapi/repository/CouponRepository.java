package br.com.couponapi.repository;

import br.com.couponapi.domain.Coupon;
import br.com.couponapi.domain.CouponStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CouponRepository {

    Coupon save(Coupon coupon);

    Optional<Coupon> findById(UUID id);

    List<Coupon> findAllByStatus(CouponStatus status);

    void deleteAll();
}
