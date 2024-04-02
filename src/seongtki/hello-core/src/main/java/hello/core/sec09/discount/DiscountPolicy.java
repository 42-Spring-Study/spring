package hello.core.sec09.discount;

import hello.core.sec09.member.Member;

public interface DiscountPolicy {
    int discount(Member member, int price);
}
