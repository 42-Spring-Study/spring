package me.staek.itemservice.domain.item;


import lombok.Data;

import java.util.List;

@Data
public class Item {
    private Long id;
    private String itemName;
    private Integer price;
    private Integer quantity;

    /**
     * 추가
     * MVC2 타임리프 스프링통합
     */
    private boolean open; // 상품판여부
    private List<String> regions; // 등록지역
    private ItemType itemType; // 상품종류
    private String deliveryCode; // 배송방식

    public Item(){}
    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }

}
