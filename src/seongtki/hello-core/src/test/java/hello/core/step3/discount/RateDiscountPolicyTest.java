package hello.core.step3.discount;

import hello.core.step3.discount.DiscountPolicy;
import hello.core.step3.discount.RateDiscountPolicy;
import hello.core.step3.member.Grade;
import hello.core.step3.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RateDiscountPolicyTest {

    private final DiscountPolicy discountPolicy = new RateDiscountPolicy();

    @Test
    @DisplayName("VIP는 10% 할인이 되어야 한다")
    void VIP할인성공() {
        // given
        Long memberId = 1L;
        Member member = new Member(memberId, "seongtki", Grade.VIP);

        // when
        int price = 10000;
        int discount = discountPolicy.discount(member, price);

        // then
        Assertions.assertThat(1000).isEqualTo(discount);
    }

    @Test
    @DisplayName("VIP가 아니면 할인아 안되어야 한다")
    void VIP할인실패() {
        // given
        Long memberId = 1L;
        Member member = new Member(memberId, "seongtki", Grade.BASIC);

        // when
        int price = 10000;
        int discount = discountPolicy.discount(member, price);

        // then
        Assertions.assertThat(0).isEqualTo(discount);
    }

}
