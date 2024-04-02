package hello.core.step3.discount;

import hello.core.step3.member.Member;

public interface DiscountPolicy {
    int discount(Member member, int price);
}
