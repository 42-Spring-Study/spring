package me.staek.itemservice.domain.item;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@Data
public class Item {
    @NotNull(groups = UpdateCheck.class)
    private Long id;

    /**
     * NotBlank.item.itemName
     * NotBlank.itemName
     * NotBlank.java.lang.String
     * NotBlank
     */
    @NotBlank(groups = {SaveCheck.class, UpdateCheck.class}, message = "필수값입니다.")
    private String itemName;
    /**
     * Range.item.price
     * Range.price
     * Range.java.lang.Integer
     * Range
     */
    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    @Range(min = 1000, max = 100000, groups = {SaveCheck.class, UpdateCheck.class})
    private Integer price;
    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    @Max(value = 9999, groups = {SaveCheck.class, UpdateCheck.class})
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
