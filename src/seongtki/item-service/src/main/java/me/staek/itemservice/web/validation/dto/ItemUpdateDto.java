package me.staek.itemservice.web.validation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import me.staek.itemservice.domain.item.ItemType;
import org.hibernate.validator.constraints.Range;

import java.util.List;


@Data
public class ItemUpdateDto {

    @NotNull
    private Long id;

    @NotBlank
    private String itemName;

    @NotNull
    @Range(min = 1000, max = 1000000)
    private Integer price;

    //수정에서는 수량은 자유롭게 변경할 수 있다.
    private Integer quantity;

    private boolean open; // 상품판여부
    private List<String> regions; // 등록지역
    private ItemType itemType; // 상품종류
    private String deliveryCode; // 배송방식

}
