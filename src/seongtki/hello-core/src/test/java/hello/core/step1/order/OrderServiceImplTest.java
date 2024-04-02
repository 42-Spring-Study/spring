package hello.core.step1.order;

import hello.core.step1.member.Grade;
import hello.core.step1.member.Member;
import hello.core.step1.member.MemberService;
import hello.core.step1.member.MemberServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderServiceImplTest {

    private final MemberService memberService = new MemberServiceImpl();
    private final OrderService orderService = new OrderServiceImpl();
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
