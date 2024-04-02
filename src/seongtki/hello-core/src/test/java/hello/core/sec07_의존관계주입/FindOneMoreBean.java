package hello.core.sec07_의존관계주입;

import hello.core.sec07.AutoAppConfig;
import hello.core.sec07.discount.DiscountPolicy;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class FindOneMoreBean {
    @Test
    public void 조회빈두개이상() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AutoAppConfig.class);
        context.getBean(DiscountPolicy.class);
    }
}
