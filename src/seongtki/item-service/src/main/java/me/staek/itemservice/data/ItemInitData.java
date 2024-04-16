package me.staek.itemservice.data;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import me.staek.itemservice.domain.item.Item;
import me.staek.itemservice.domain.item.ItemRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemInitData {

    private final ItemRepository repository;

    @PostConstruct
    public void init() {
        repository.save(new Item("testA", 10000, 10));
        repository.save(new Item("testB", 20000, 20));
    }
}
