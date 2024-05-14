package me.staek.itemservice.config;

import lombok.RequiredArgsConstructor;
import me.staek.itemservice.domain.item.ItemRepository;
import me.staek.itemservice.domain.item.ItemService;
import me.staek.itemservice.domain.item.ItemServiceV1;
import me.staek.itemservice.domain.item.jpa.JpaItemRepositoryV2;
import me.staek.itemservice.domain.item.jpa.SpringDataJpaItemRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SpringDataJpaConfig {
    private final SpringDataJpaItemRepository springDataJpaItemRepository;

    @Bean
    public ItemService itemService() {
        return new ItemServiceV1(itemRepository());
    }
    @Bean
    public ItemRepository itemRepository() {
        return new JpaItemRepositoryV2(springDataJpaItemRepository);
    }
}
