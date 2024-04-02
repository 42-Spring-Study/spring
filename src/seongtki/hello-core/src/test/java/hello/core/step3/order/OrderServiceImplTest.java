package hello.core.step3.order;

import hello.core.step3.AppConfig;
import hello.core.step3.member.Grade;
import hello.core.step3.member.Member;
import hello.core.step3.member.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

class OrderServiceImplTest {
    private MemberService memberService;
    private OrderService orderService;

    @BeforeEach
    void beforeEach() {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        memberService = context.getBean("memberService", MemberService.class);
        orderService = context.getBean("orderService", OrderService.class);
    }

    @Test
    void createOrder() {
        // given
        Long memberId = 1L;
        Member member = new Member(memberId, "seongtki", Grade.VIP);
//        Member member = new Member(memberId, "seongtki", Grade.BASIC);
        memberService.join(member);

        // when
        Order order01 = orderService.createOrder(memberId, "item01", 20000);
        int ordredPrice = order01.calculatedPrice();
        // then
        Assertions.assertThat(19000).isEqualTo(ordredPrice);
    }
}
