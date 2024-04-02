package hello.core.step1.order;

import hello.core.step1.discount.DiscountPolicy;
import hello.core.step1.discount.FixDiscountPolicy;
import hello.core.step1.member.*;

public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository = new MemeryMemberRepository();
    private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discount = discountPolicy.discount(member);
        return new Order(member.getId(), itemName, itemPrice, discount);
    }
}
