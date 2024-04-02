package hello.core.step1.order;

public interface OrderService {
    Order createOrder(Long memberId, String itemName, int itemPrice);
}
