package me.staek.itemservice.domain.item;

import lombok.extern.slf4j.Slf4j;
import me.staek.itemservice.web.validation.dto.ItemSearchCond;
import me.staek.itemservice.web.validation.dto.ItemUpdateDto;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * 데이터 접근 기술
 * - JDBC & 메모리 디비
 *
 * 쿼리와 Mapper를 작성해서 JdbcTemplate에 넘겨주면 동작하는 형식이다.
 * 쿼리 작성 시 인자를 바인딩 할 때 컬럼 순서 검증절차가 없어서 잠재적인 에러가 발생할 수 있다.
 * 동적 쿼리 작성이 까다롭다.
 */
@Slf4j
@Repository
public class JdbcTemplateItemRepositoryV1 implements ItemRepository {
    private final JdbcTemplate template;

    public JdbcTemplateItemRepositoryV1(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public Item save(Item item) {
        String sql = "insert into item (item_name, price, quantity) values (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            //자동 증가 키
            PreparedStatement ps = connection.prepareStatement(sql, new String[] {"id"});
            ps.setString(1, item.getItemName());
            ps.setInt(2, item.getPrice());
            ps.setInt(3, item.getQuantity());
            return ps;
        }, keyHolder);
        long key = keyHolder.getKey().longValue();
        item.setId(key);
        return item;
    }

    @Override
    public Optional<Item> findById(Long id) {
        String sql = "select id, item_name, price, quantity from item where id = ?";

        try {
            Item item = template.queryForObject(sql, itemRowMapper(), id);
            return Optional.of(item);
        } catch(EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private RowMapper<Item> itemRowMapper() {
        return new RowMapper<Item>() {
            @Override
            public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
                Item item = new Item();
                item.setId(rs.getLong("id"));
                item.setItemName(rs.getString("item_name"));
                item.setPrice(rs.getInt("price"));
                item.setQuantity(rs.getInt("quantity"));
                return item;
            }
        };

    }

    @Override
    public List<Item> findAll() {
        String sql = "select id, item_name, price, quantity from item";
        return template.query(sql, itemRowMapper());
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();
        String sql = "select id, item_name, price, quantity from item";


        if (StringUtils.hasText(itemName) || maxPrice != null) {
            sql += " where";
        }
        boolean andFlag = false;
        List<Object> param = new ArrayList<>();
        if (StringUtils.hasText(itemName)) {
            sql += " item_name like concat('%',?,'%')";
            param.add(itemName);
            andFlag = true;
        }
        if (maxPrice != null) {
            if (andFlag) {
                sql += " and";
            }
            sql += " price <= ?";
            param.add(maxPrice);
        }
        log.info("sql={}", sql);
        return template.query(sql, itemRowMapper(), param.toArray());
    }

    @Override
    public void update(Long id, ItemUpdateDto uptItem) {
        String sql = "update item set item_name=?, price=?, quantity=? where id=?";

        template.update(sql, uptItem.getItemName()
                            , uptItem.getPrice()
                            , uptItem.getQuantity()
                            , uptItem.isOpen()
                            , uptItem.getItemType()
                            , uptItem.getRegions()
                            , uptItem.getDeliveryCode());

    }
}
