package br.com.couponapi.repository;

import br.com.couponapi.domain.Coupon;
import br.com.couponapi.domain.CouponStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CouponRepository extends JpaRepository<Coupon, UUID> {

    List<Coupon> findAllByStatus(CouponStatus status);
}
