package me.staek.itemservice.domain.item.mybatis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.staek.itemservice.domain.item.Item;
import me.staek.itemservice.domain.item.ItemRepository;
import me.staek.itemservice.web.validation.dto.ItemSearchCond;
import me.staek.itemservice.web.validation.dto.ItemUpdateDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 데이터 접근 기술
 * - mybatis & 메모리 디비
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class MyBatisItemRepository implements ItemRepository {

    private final ItemMapper itemMapper;


    @Override
    public Item save(Item item) {
        log.info("itemMapper class={}", itemMapper.getClass());
        itemMapper.save(item);
        return item;
    }

    @Override
    public Optional<Item> findById(Long id) {
        return this.itemMapper.findById(id);
    }

    @Override
    public List<Item> findAll() {
        return this.itemMapper.findAll();
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        return this.itemMapper.findAll(cond);
    }

    @Override
    public void update(Long id, ItemUpdateDto uptItem) {
        this.itemMapper.update(id, uptItem);
    }
}
