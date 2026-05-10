package br.com.couponapi.api;

import br.com.couponapi.repository.CouponRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CouponControllerTest {

    private static final String VALID_COUPON = """
            {
              "code": "abc-123",
              "description": "Cupom de teste",
              "discountValue": 0.8,
              "expirationDate": "2026-05-09T10:00:00Z",
              "published": true
            }
            """;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CouponRepository couponRepository;

    @BeforeEach
    void setUp() {
        couponRepository.deleteAll();
    }

    @Test
    void shouldCreateCouponWithSanitizedCode() throws Exception {
        mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_COUPON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("ABC123"))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.published").value(true));
    }

    @Test
    void shouldRejectCouponWithPastExpirationDate() throws Exception {
        String request = """
                {
                  "code": "abc123",
                  "description": "Cupom vencido",
                  "discountValue": 0.8,
                  "expirationDate": "2026-05-07T10:00:00Z"
                }
                """;

        mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("expiration date cannot be in the past"));
    }

    @Test
    void shouldSoftDeleteCouponAndRejectSecondDelete() throws Exception {
        String response = mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_COUPON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String id = readId(response);

        mockMvc.perform(delete("/coupon/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/coupon/{id}", id))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("coupon is already deleted"));
    }

    @Test
    void shouldListOnlyActiveCoupons() throws Exception {
        String firstResponse = mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_COUPON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String secondCoupon = VALID_COUPON.replace("abc-123", "xyz-789");

        mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(secondCoupon))
                .andExpect(status().isCreated());

        String id = readId(firstResponse);

        mockMvc.perform(delete("/coupon/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/coupon"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].code").value("XYZ789"));
    }

    private String readId(String response) throws Exception {
        return JsonPath.read(response, "$.id");
    }

    @TestConfiguration
    static class FixedClockConfig {

        @Bean
        @Primary
        Clock fixedClock() {
            return Clock.fixed(Instant.parse("2026-05-08T10:00:00Z"), ZoneOffset.UTC);
        }
    }
}
