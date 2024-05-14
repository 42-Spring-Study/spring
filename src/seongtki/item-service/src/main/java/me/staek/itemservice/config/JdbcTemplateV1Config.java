package me.staek.itemservice.config;

import lombok.RequiredArgsConstructor;
import me.staek.itemservice.domain.item.ItemRepository;
import me.staek.itemservice.domain.item.ItemService;
import me.staek.itemservice.domain.item.ItemServiceV1;
import me.staek.itemservice.domain.item.JdbcTemplateItemRepositoryV1;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class JdbcTemplateV1Config {
    private final DataSource dataSource;

    @Bean
    public ItemService itemService() {
        return new ItemServiceV1(itemRepository());
    }

    @Bean
    public ItemRepository itemRepository() {
        return new JdbcTemplateItemRepositoryV1(dataSource);
    }
}
