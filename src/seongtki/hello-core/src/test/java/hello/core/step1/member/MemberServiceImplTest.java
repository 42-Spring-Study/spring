package hello.core.step1.member;

import hello.core.step1.member.Grade;
import hello.core.step1.member.Member;
import hello.core.step1.member.MemberService;
import hello.core.step1.member.MemberServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MemberServiceImplTest {

    MemberService memberService = new MemberServiceImpl();

    @Test
    void join() {
        // given
        Member member = new Member(1L, "seongtki", Grade.VIP);

        // when
        memberService.join(member);
        Member finded = memberService.findMember(1L);

        // then
        Assertions.assertThat(member).isEqualTo(finded);

    }

    @Test
    void findMember() {
    }
}
