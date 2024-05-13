package me.staek.itemservice.domain.item;

import me.staek.itemservice.web.validation.dto.ItemSearchCond;
import me.staek.itemservice.web.validation.dto.ItemUpdateDto;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 데이터 접근 기술
 * - 메모리
 */
@Repository
public class MemoryItemRepository implements ItemRepository {

    private static final Map<Long, Item> store = new HashMap<>();

    private static long sequence = 0L;

    @Override
    public Item save(Item item) {
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    @Override
    public Optional<Item> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Item> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();
        List<Item> collect = store.values().stream()
                .filter(item -> {
                    if (ObjectUtils.isEmpty(itemName))
                        return true;
                    return item.getItemName().contains(itemName);
                }).filter(item -> {
                    if (ObjectUtils.isEmpty(maxPrice))
                        return true;
                    return item.getPrice() <= maxPrice;
                }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public void update(Long id, ItemUpdateDto uptItem) {
        Item item = this.findById(id).orElseThrow();
        item.setItemName(uptItem.getItemName());
        item.setPrice(uptItem.getPrice());
        item.setQuantity(uptItem.getQuantity());
        item.setOpen(uptItem.isOpen());
        item.setItemType(uptItem.getItemType());
        item.setRegions(uptItem.getRegions());
        item.setDeliveryCode(uptItem.getDeliveryCode());
    }

    /**
     * 메모리맵 관리 상황에서만 사용되는 함수
     */
    public void clearStore() {
        store.clear();
    }
}
