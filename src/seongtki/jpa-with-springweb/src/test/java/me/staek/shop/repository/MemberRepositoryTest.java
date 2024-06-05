package me.staek.shop.repository;

import me.staek.shop.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringRunner.class)
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(false)
    void test() {
        Member member = new Member();
        member.setName("seongtki");

        Long id = memberRepository.save(member);


        Member found = memberRepository.findOne(id);

        Assertions.assertThat(found.getId()).isEqualTo(member.getId());
        Assertions.assertThat(found.getName()).isEqualTo(member.getName());
        Assertions.assertThat(found).isEqualTo(member);
    }



}
