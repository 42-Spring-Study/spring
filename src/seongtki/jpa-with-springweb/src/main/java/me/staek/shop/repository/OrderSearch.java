package me.staek.shop.repository;


import lombok.Getter;
import lombok.Setter;
import me.staek.shop.domain.OrderStatus;

@Getter
@Setter
public class OrderSearch {

    private String memberName;
    private OrderStatus orderStatus;
}
