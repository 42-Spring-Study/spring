package me.staek.itemservice.domain.item;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemRepositoryTest {

    ItemRepository repository = new ItemRepository();

    @AfterEach
    public void afterEach() {
        repository.clearStore();
    }

    @Test
    public void save() {
        Item item = new Item("itemA", 10000, 10);

        Item saved = repository.save(item);

        Item finded = repository.findByItem(item.getId());
        Assertions.assertThat(finded).isEqualTo(saved);
    }

    @Test
    void findAll() {
        Item item1 = new Item("itemA", 10000, 10);
        Item item2 = new Item("itemB", 20000, 20);
        repository.save(item1);
        repository.save(item2);

        List<Item> list = repository.findAll();

        Assertions.assertThat(list.size()).isEqualTo(2);
        Assertions.assertThat(list).contains(item1, item2);
    }

    @Test
    void updateItem() {
        Item item = new Item("itemA", 10000, 10);
        Item saved = repository.save(item);


        Item updateItem = new Item("itemB", 20000, 20);
        repository.update(saved.getId(), updateItem);

        Item updated = repository.findByItem(saved.getId());
        Assertions.assertThat(updated.getItemName()).isEqualTo(saved.getItemName());
        Assertions.assertThat(updated.getPrice()).isEqualTo(saved.getPrice());
        Assertions.assertThat(updated.getQuantity()).isEqualTo(saved.getQuantity());
    }

}
