package me.staek.itemservice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepository {

    private static final Map<Long, Item> store = new HashMap<>();

    private static long sequence = 0L;

    public Item save(Item item) {
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    public Item findByItem(Long id) {
        return store.get(id);
    }

    public List<Item> findAll() {
        return new ArrayList<>(store.values());
    }

    public void update(Long id, Item uptItem) {
        Item item = this.findByItem(id);
        item.setItemName(uptItem.getItemName());
        item.setPrice(uptItem.getPrice());
        item.setQuantity(uptItem.getQuantity());
    }

    public void clearStore() {
        store.clear();
    }
}
