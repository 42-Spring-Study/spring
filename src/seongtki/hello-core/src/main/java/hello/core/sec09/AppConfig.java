//package hello.core.sec09;
//
//import hello.core.sec09.discount.DiscountPolicy;
//import hello.core.sec09.discount.FixDiscountPolicy;
//import hello.core.sec09.member.MemberService;
//import hello.core.sec09.member.MemberServiceImpl;
//import hello.core.sec09.member.MemeryMemberRepository;
//import hello.core.sec09.order.OrderService;
//import hello.core.sec09.order.OrderServiceImpl;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class AppConfig {
//
//    @Bean
//    public MemberService memberService() {
//        return new MemberServiceImpl(memberRepository());
//    }
//
//    @Bean
//    public OrderService orderService() {
//        return new OrderServiceImpl(memberRepository(), discountPolicy());
//    }
//
//    @Bean
//    public DiscountPolicy discountPolicy() {
//        return new FixDiscountPolicy();
////        return new RateDiscountPolicy();
//    }
//
//    @Bean
//    public MemeryMemberRepository memberRepository() {
//        return new MemeryMemberRepository();
//    }
//}
