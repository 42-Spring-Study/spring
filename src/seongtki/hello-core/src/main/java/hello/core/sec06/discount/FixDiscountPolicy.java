package hello.core.sec06.discount;

import hello.core.sec06.member.Grade;
import hello.core.sec06.member.Member;
import org.springframework.stereotype.Component;

@Component
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
