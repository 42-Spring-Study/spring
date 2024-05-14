package me.staek.itemservice.domain.item.jpa;

import me.staek.itemservice.domain.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpringDataJpaItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByItemNameLike(String itemName);
    List<Item> findByPriceLessThanEqual(Integer price);

    /**
     * 메서드 이름으로 쿼리를 실행하는 기능은 다음과 같은 단점이 있다.
     * 1. 조건이 많으면 메서드 이름이 너무 길어진다.
     * 2. 조인 같은 복잡한 조건을 사용할 수 없다.
     * => 복잡해지면 직접 JPQL 쿼리를 작성하는것이 좋다.
     */
    List<Item> findByItemNameLikeAndPriceLessThanEqual(String itemName, Integer price);


    /**
     * 메서드 이름으로 쿼리를 실행할 때는 파라미터를 순서대로 입력하면 되지만, 쿼리를 직접 실행할 때는 파라미터를
     * 명시적으로 바인딩 해야 한다.
     * 파라미터 바인딩은 @Param("itemName") 애노테이션을 사용하고, 애노테이션의 값에 파라미터 이름을 주면
     * 된다
     */
    //쿼리 직접 실행
    @Query("select i from Item i where i.itemName like :itemName and i.price <= :price")
    List<Item> findItems(@Param("itemName") String itemName, @Param("price") Integer price);

}
