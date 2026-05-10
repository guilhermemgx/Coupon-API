package br.com.couponapi.api;

import br.com.couponapi.api.dto.CouponResponse;
import br.com.couponapi.api.dto.CreateCouponRequest;
import br.com.couponapi.domain.Coupon;
import br.com.couponapi.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CouponResponse create(@RequestBody @Valid CreateCouponRequest request) {
        Coupon coupon = couponService.create(request);
        return CouponResponse.from(coupon);
    }

    @GetMapping
    public List<CouponResponse> findActive() {
        return couponService.findActive();
    }

    @GetMapping("/{id}")
    public CouponResponse findById(@PathVariable UUID id) {
        Coupon coupon = couponService.findById(id);
        return CouponResponse.from(coupon);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        couponService.delete(id);
    }
}
