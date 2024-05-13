package me.staek.itemservice.config;

import me.staek.itemservice.domain.item.ItemRepository;
import me.staek.itemservice.domain.item.ItemService;
import me.staek.itemservice.domain.item.ItemServiceV1;
import me.staek.itemservice.domain.item.MemoryItemRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ItemService
 * ItemRepository (MemoryItemRepository)
 */
@Configuration
public class MemoryConfig {

    @Bean
    public ItemService itemService() {
        return new ItemServiceV1(itemRepository());
    }

    @Bean
    public ItemRepository itemRepository() {
        return new MemoryItemRepository();
    }
}
