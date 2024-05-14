package me.staek.itemservice.config;

import jakarta.persistence.EntityManager;
import me.staek.itemservice.domain.item.ItemRepository;
import me.staek.itemservice.domain.item.ItemService;
import me.staek.itemservice.domain.item.ItemServiceV1;
import me.staek.itemservice.domain.item.jpa.JpaItemRepositoryV1;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JpaConfig {
    private final EntityManager em;

    public JpaConfig(EntityManager em) {
        this.em = em;
    }
    @Bean
    public ItemService itemService() {
        return new ItemServiceV1(itemRepository());
    }
    @Bean
    public ItemRepository itemRepository() {
        return new JpaItemRepositoryV1(em);
    }
}
