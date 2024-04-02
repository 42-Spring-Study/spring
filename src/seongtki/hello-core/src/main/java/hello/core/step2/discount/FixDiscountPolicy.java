package hello.core.step2.discount;

import hello.core.step2.member.Grade;
import hello.core.step2.member.Member;

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
