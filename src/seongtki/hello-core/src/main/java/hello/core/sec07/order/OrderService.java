package hello.core.sec07.order;

public interface OrderService {
    Order createOrder(Long memberId, String itemName, int itemPrice);
}
