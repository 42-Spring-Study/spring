package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
@Transactional
public class MemberServiceIntegrationTest {

    @Autowired MemberService memberService;
//    @Autowired MemberRepository memberRepository;

    @Test
    void 회원가입() {

        // given
        Member member = new Member("seongtki");

        // when
        Long id = memberService.join(member);

        // then
        Optional<Member> finded = memberService.findOne(id);
        Assertions.assertThat(finded.get().getName()).isEqualTo(member.getName());
    }

    @Test
    public void 중복_회원_예외() {
        // given
        Member member = new Member("seongtki");

        // when
        Long id = memberService.join(member);

        // then
        org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class, () -> memberService.join(member));
    }
}
