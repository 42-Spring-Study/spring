package hello.core.step1.discount;

import hello.core.step1.member.Member;

public interface DiscountPolicy {
    int discount(Member member);
}
