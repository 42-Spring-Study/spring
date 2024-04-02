package hello.core.sec07.discount;

import hello.core.sec07.member.Grade;
import hello.core.sec07.member.Member;
import org.springframework.stereotype.Component;

@Component
public class RateDiscountPolicy implements DiscountPolicy {

    private final int rate = 10;
    @Override
    public int discount(Member member, int price) {
        if (member.getGrade() == Grade.VIP)
            return price * rate / 100;
        return 0;
    }
}
