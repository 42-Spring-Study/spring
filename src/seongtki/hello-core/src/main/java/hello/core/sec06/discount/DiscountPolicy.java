package hello.core.sec06.discount;

import hello.core.sec06.member.Member;

public interface DiscountPolicy {
    int discount(Member member, int price);
}
