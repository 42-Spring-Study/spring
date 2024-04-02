package hello.core.step3.discount;

import hello.core.step3.member.Grade;
import hello.core.step3.member.Member;

public class RateDiscountPolicy implements DiscountPolicy {

    private final int rate = 10;
    @Override
    public int discount(Member member, int price) {
        if (member.getGrade() == Grade.VIP)
            return price * rate / 100;
        return 0;
    }
}
