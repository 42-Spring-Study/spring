package hello.core.step1.discount;

import hello.core.step1.member.Grade;
import hello.core.step1.member.Member;

public class FixDiscountPolicy implements DiscountPolicy {
    final int discountPrice = 1000;
    @Override
    public int discount(Member member) {
        if (member.getGrade() == Grade.VIP)
            return discountPrice;
        else
            return 0;
    }
}
