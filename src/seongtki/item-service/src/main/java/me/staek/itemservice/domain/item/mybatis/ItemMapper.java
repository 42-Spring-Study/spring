package me.staek.itemservice.domain.item.mybatis;

import me.staek.itemservice.domain.item.Item;
import me.staek.itemservice.web.validation.dto.ItemSearchCond;
import me.staek.itemservice.web.validation.dto.ItemUpdateDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * 마이바티스 매핑 XML을 호출해주는 매퍼 인터페이스이다.
 * 이 인터페이스에는 @Mapper 애노테이션을 붙여주어야 한다. 그래야 MyBatis에서 인식할 수 있다.
 * 이 인터페이스의 메서드를 호출하면 다음에 보이는 xml 의 해당 SQL을 실행하고 결과를 돌려준다.
 * ItemMapper 인터페이스의 구현체에 대한 부분은 뒤에 별도로 설명한다
 */
@Mapper
public interface ItemMapper {
    void save(Item item);
    void update(@Param("id") Long id, @Param("updateParam")ItemUpdateDto itemUpdateDto);
    Optional<Item> findById(Long id);
    List<Item> findAll(ItemSearchCond cond);
    List<Item> findAll();
}
