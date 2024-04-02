package hello.core.step2;

import hello.core.step2.discount.DiscountPolicy;
import hello.core.step2.discount.FixDiscountPolicy;
import hello.core.step2.discount.RateDiscountPolicy;
import hello.core.step2.member.MemberService;
import hello.core.step2.member.MemberServiceImpl;
import hello.core.step2.member.MemeryMemberRepository;
import hello.core.step2.order.OrderService;
import hello.core.step2.order.OrderServiceImpl;

public class AppConfig {
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }
    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    public DiscountPolicy discountPolicy() {
        return new FixDiscountPolicy();
//        return new RateDiscountPolicy();
    }

    public MemeryMemberRepository memberRepository() {
        return new MemeryMemberRepository();
    }
}
