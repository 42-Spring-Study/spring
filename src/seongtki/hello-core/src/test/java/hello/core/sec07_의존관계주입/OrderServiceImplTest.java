package hello.core.sec07_의존관계주입;

import hello.core.sec07.AutoAppConfig;
import hello.core.sec07.discount.DiscountPolicy;
import hello.core.sec07.discount.FixDiscountPolicy;
import hello.core.sec07.discount.RateDiscountPolicy;
import hello.core.sec07.member.Grade;
import hello.core.sec07.member.Member;
import hello.core.sec07.member.MemeryMemberRepository;
import hello.core.sec07.order.Order;
import hello.core.sec07.order.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class OrderServiceImplTest {
    @Test
    void createOrder() {
        MemeryMemberRepository repository = new MemeryMemberRepository();
        repository.save(new Member(1L, "seongtki", Grade.VIP));
//        OrderServiceImpl orderService = new OrderServiceImpl(repository, new FixDiscountPolicy());
        OrderServiceImpl orderService = new OrderServiceImpl(repository, new RateDiscountPolicy());
        Order order = orderService.createOrder(1L, "itemA", 10000);
        System.out.println(order.getDiscountPrice());
    }


    @Test
    void createOrder2() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AutoAppConfig.class);
        OrderServiceImpl bean = context.getBean(OrderServiceImpl.class);
        MemeryMemberRepository repository = new MemeryMemberRepository();
        repository.save(new Member(1L, "seongtki", Grade.VIP));
        Order order = bean.createOrder(1L, "itemA", 10000);
        System.out.println(order.getDiscountPrice());
    }

    /*
    @Test
    void fieldInjectTest() {
        OrderServiceImpl orderService = new OrderServiceImpl();
        orderService.setMemberRepository(new MemeryMemberRepository());
        orderService.setDiscountPolicy(new FixDiscountPolicy());
        orderService.createOrder(1L, "itemA", 10000);
    }
    */
}
