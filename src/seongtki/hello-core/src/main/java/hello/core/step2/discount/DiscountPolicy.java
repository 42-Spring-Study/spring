package hello.core.step2.discount;

import hello.core.step2.member.Member;

public interface DiscountPolicy {
    int discount(Member member, int price);
}
