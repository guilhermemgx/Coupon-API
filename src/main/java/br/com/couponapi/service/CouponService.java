package br.com.couponapi.service;

import br.com.couponapi.api.dto.CouponResponse;
import br.com.couponapi.api.dto.CreateCouponRequest;
import br.com.couponapi.domain.Coupon;
import br.com.couponapi.domain.CouponStatus;
import br.com.couponapi.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final Clock clock;

    public Coupon create(CreateCouponRequest request) {
        OffsetDateTime now = OffsetDateTime.now(clock);

        Coupon coupon = new Coupon(
                request.code(),
                request.description(),
                request.discountValue(),
                request.expirationDate(),
                Boolean.TRUE.equals(request.published()),
                now
        );

        return couponRepository.save(coupon);
    }

    public Coupon findById(UUID id) {
        return couponRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Coupon not found"));
    }

    public List<CouponResponse> findActive() {
        return couponRepository.findAllByStatus(CouponStatus.ACTIVE)
                .stream()
                .map(CouponResponse::from)
                .toList();
    }

    public void delete(UUID id) {
        Coupon coupon = findById(id);
        coupon.delete(OffsetDateTime.now(clock));
        couponRepository.save(coupon);
    }
}
