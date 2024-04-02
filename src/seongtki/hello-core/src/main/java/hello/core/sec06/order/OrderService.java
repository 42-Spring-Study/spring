package hello.core.sec06.order;

public interface OrderService {
    Order createOrder(Long memberId, String itemName, int itemPrice);
}
