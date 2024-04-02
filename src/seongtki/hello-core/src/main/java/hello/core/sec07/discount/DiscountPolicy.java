package hello.core.sec07.discount;

import hello.core.sec07.member.Member;

public interface DiscountPolicy {
    int discount(Member member, int price);
}
