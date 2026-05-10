package br.com.couponapi.repository.jpa;

import br.com.couponapi.domain.CouponStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataCouponRepository extends JpaRepository<CouponEntity, UUID> {

    List<CouponEntity> findAllByStatus(CouponStatus status);
}
