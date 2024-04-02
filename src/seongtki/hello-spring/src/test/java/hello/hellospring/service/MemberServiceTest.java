package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemoryMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MemberServiceTest {
    MemberService memberService;

    /**
     * 테스트에 직접 사용하지 않지만, 회원관리 리소스가 static 형태라서
     * 단위테스트마다 claer작업이 필요하여 작성함.
     */
    MemoryMemberRepository memberRepository;

    @BeforeEach
    void beforeEach() {
        memberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memberRepository);
    }
    @AfterEach
    void afterEach() {
        memberRepository.clearStore();
    }

    @Test
    void join() {
        // given
        Member member = new Member("seongtki");

        // when
        Long id = memberService.join(member);
        Optional<Member> finded = memberService.findOne(id);

        // then
        Assertions.assertThat(member).isEqualTo(finded.get());
    }

    @Test
    void findMembers() {
        // given
        Member m1 = new Member( "seongtki");
        Member m2 = new Member( "seongtaekkim");

        // when
        memberService.join(m1);
        memberService.join(m2);
        List<Member> finded = memberService.findMembers();

        // then
        Assertions.assertThat(2).isEqualTo(finded.size());
    }

    @Test
    void findOne() {
        // given
        Member m1 = new Member( "seongtki");

        // when
        memberService.join(m1);
        Optional<Member> finded = memberService.findOne(m1.getId());

        // then
        Assertions.assertThat(m1).isEqualTo(finded.get());
    }

    @Test
    void 중복회원체크() {

        // given
        Member m1 = new Member( "seongtki");

        // when
        memberService.join(m1);

        // then
        assertThrows(IllegalStateException.class, () -> memberService.join(m1));
        Assertions.assertThat(1).isEqualTo(memberService.findMembers().size());
    }
}
