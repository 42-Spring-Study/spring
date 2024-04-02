package hello.core.sec09.discount;

import hello.core.step2.discount.DiscountPolicy;
import hello.core.step2.member.Grade;
import hello.core.step2.member.Member;

public class RateDiscountPolicy implements DiscountPolicy {

    private final int rate = 10;
    @Override
    public int discount(Member member, int price) {
        if (member.getGrade() == Grade.VIP)
            return price * rate / 100;
        return 0;
    }
}
