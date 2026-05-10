package br.com.couponapi.repository.jpa;

import br.com.couponapi.domain.Coupon;
import br.com.couponapi.domain.CouponStatus;
import br.com.couponapi.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaCouponRepository implements CouponRepository {

    private final SpringDataCouponRepository springDataCouponRepository;
    private final CouponEntityMapper couponEntityMapper;

    @Override
    public Coupon save(Coupon coupon) {
        CouponEntity entity = couponEntityMapper.toEntity(coupon);
        CouponEntity savedEntity = springDataCouponRepository.save(entity);
        return couponEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Coupon> findById(UUID id) {
        return springDataCouponRepository.findById(id)
                .map(couponEntityMapper::toDomain);
    }

    @Override
    public List<Coupon> findAllByStatus(CouponStatus status) {
        return springDataCouponRepository.findAllByStatus(status)
                .stream()
                .map(couponEntityMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteAll() {
        springDataCouponRepository.deleteAll();
    }
}
