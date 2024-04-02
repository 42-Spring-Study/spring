package hello.core.sec07.discount;

import hello.core.sec07.annotation.MainDiscountPolicy;
import hello.core.sec07.member.Grade;
import hello.core.sec07.member.Member;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

//@Primary
@Component
@MainDiscountPolicy
public class FixDiscountPolicy implements DiscountPolicy {
    private final int discountPrice = 1000;
    @Override
    public int discount(Member member, int price) {
        if (member.getGrade() == Grade.VIP)
            return discountPrice;
        else
            return 0;
    }
}
