package hello.core.step3.order;

public interface OrderService {
    Order createOrder(Long memberId, String itemName, int itemPrice);
}
