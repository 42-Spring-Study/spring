package hello.core.step2.order;

import hello.core.step2.AppConfig;
import hello.core.step2.member.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceImplTest {
    private MemberService memberService;
    private OrderService orderService;

    @BeforeEach
    void beforeEach() {
        AppConfig appConfig = new AppConfig();
        memberService = appConfig.memberService();
        orderService = appConfig.orderService();
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
