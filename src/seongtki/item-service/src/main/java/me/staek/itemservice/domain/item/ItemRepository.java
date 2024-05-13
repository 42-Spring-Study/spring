package me.staek.itemservice.domain.item;

import me.staek.itemservice.web.validation.dto.ItemSearchCond;
import me.staek.itemservice.web.validation.dto.ItemUpdateDto;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item save(Item item);
    Optional<Item> findById(Long id);
    List<Item> findAll();
    List<Item> findAll(ItemSearchCond cond);
    void update(Long id, ItemUpdateDto uptItem);
}
