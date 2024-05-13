package me.staek.itemservice.data;

import lombok.RequiredArgsConstructor;
import me.staek.itemservice.domain.item.Item;
import me.staek.itemservice.domain.item.ItemRepository;
import me.staek.itemservice.domain.member.Member;
import me.staek.itemservice.domain.member.MemberRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

/**
 * before
 * 빈설정 후 PostConstruct 에서 테스트 데이터를 생성
 *
 * after
 * PostConstruct 시점에 AOP 등 설정완료 안된 부분이 있을 수 있어서
 * @EventListener(ApplicationReadyEvent.class) 로 변경
 *  - 컨테이너가 완전히 초기화 된 후 호출
 */
//@Component
@RequiredArgsConstructor
public class ItemInitData {

    private final ItemRepository memoryItemRepository;
    private final MemberRepository memberRepository;

//    @PostConstruct
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        memoryItemRepository.save(new Item("testA", 10000, 10));
        memoryItemRepository.save(new Item("testB", 20000, 20));
        memberRepository.save(new Member("seongtki", "1234", "satek"));
    }

}
