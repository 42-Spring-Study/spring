package me.staek.itemservice.domain.item.jpa;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.staek.itemservice.domain.item.Item;
import me.staek.itemservice.domain.item.ItemRepository;
import me.staek.itemservice.web.validation.dto.ItemSearchCond;
import me.staek.itemservice.web.validation.dto.ItemUpdateDto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import  me.staek.itemservice.domain.item.QItem;

/**
 * 데이터 접근 기술
 *  - jpa & querydsl & 메모리 디비
 * - 기본적으로 jpa를 사용하고, 쿼리작성이 필요한 경우에 (특히 동적쿼리) querydsl을 사용한다.
 *
 * querydsl
 * - 컴파일 시 querydsl에 사용될 객체 QItem가 generated에 생성된다.
 * 이를 이용해 기능을 작성한다.
 */
@Slf4j
@Repository
@Transactional
public class JpaItemRepositoryV3 implements ItemRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public JpaItemRepositoryV3(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public Item save(Item item) {
        em.persist(item);
        return item;
    }

    @Override
    public Optional<Item> findById(Long id) {
        Item item = em.find(Item.class, id);
        return Optional.ofNullable(item);
    }

    @Override
    public List<Item> findAll() {
        QItem item = QItem.item;

        List<Item> result;
        result = query
                .select(item)
                .from(item).fetch();
        return result;
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();
        QItem item = QItem.item;
        BooleanBuilder builder = new BooleanBuilder();
        if (StringUtils.hasText(itemName)) {
            builder.and(item.itemName.like("%" + itemName + "%"));
        }
        if (maxPrice != null) {
            builder.and(item.price.loe(maxPrice));
        }
        List<Item> result = query
                .select(item)
                .from(item)
                .where(builder)
                .fetch();
        return result;
    }

    @Override
    public void update(Long id, ItemUpdateDto uptItem) {
        Item findItem = findById(id).orElseThrow();
        findItem.setItemName(uptItem.getItemName());
        findItem.setPrice(uptItem.getPrice());
        findItem.setQuantity(uptItem.getQuantity());
    }
}
