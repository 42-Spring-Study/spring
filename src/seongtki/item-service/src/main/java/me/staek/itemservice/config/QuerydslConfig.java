package me.staek.itemservice.config;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import me.staek.itemservice.domain.item.ItemRepository;
import me.staek.itemservice.domain.item.ItemService;
import me.staek.itemservice.domain.item.ItemServiceV1;
import me.staek.itemservice.domain.item.jpa.JpaItemRepositoryV3;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class QuerydslConfig {
    private final EntityManager em;
    @Bean
    public ItemService itemService() {
        return new ItemServiceV1(itemRepository());
    }
    @Bean
    public ItemRepository itemRepository() {
        return new JpaItemRepositoryV3(em);
    }
}
