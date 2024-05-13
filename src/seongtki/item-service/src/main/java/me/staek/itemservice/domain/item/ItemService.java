package me.staek.itemservice.domain.item;

import me.staek.itemservice.web.validation.dto.ItemSearchCond;
import me.staek.itemservice.web.validation.dto.ItemUpdateDto;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    Item save(Item item);
    void update(Long itemId, ItemUpdateDto updateParam);
    Optional<Item> findById(Long id);
    List<Item> findItems(ItemSearchCond itemSearch);
}
