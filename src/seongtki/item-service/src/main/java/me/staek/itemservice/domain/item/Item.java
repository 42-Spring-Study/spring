package me.staek.itemservice.domain.item;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@Data
public class Item {
    private Long id;
    @NotBlank
    private String itemName;
    @NotNull @Range(min = 1000, max = 100000)
    private Integer price;
    @NotNull @Max(9999)
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
