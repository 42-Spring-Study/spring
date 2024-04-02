package hello.core.step3;

import hello.core.step3.discount.DiscountPolicy;
import hello.core.step3.discount.FixDiscountPolicy;
import hello.core.step3.member.MemberService;
import hello.core.step3.member.MemberServiceImpl;
import hello.core.step3.member.MemeryMemberRepository;
import hello.core.step3.order.OrderService;
import hello.core.step3.order.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    @Bean
    public DiscountPolicy discountPolicy() {
        return new FixDiscountPolicy();
//        return new RateDiscountPolicy();
    }

    @Bean
    public MemeryMemberRepository memberRepository() {
        return new MemeryMemberRepository();
    }
}
