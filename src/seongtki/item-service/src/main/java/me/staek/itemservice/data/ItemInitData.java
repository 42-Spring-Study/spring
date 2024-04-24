package me.staek.itemservice.data;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import me.staek.itemservice.domain.item.Item;
import me.staek.itemservice.domain.item.ItemRepository;
import me.staek.itemservice.domain.member.Member;
import me.staek.itemservice.domain.member.MemberRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemInitData {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    @PostConstruct
    public void init() {
        itemRepository.save(new Item("testA", 10000, 10));
        itemRepository.save(new Item("testB", 20000, 20));


        memberRepository.save(new Member("seongtki", "1234", "satek"));
    }

}
