package hello.core.step2.member;

import hello.core.step2.AppConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberServiceImplTest {

    private MemberService memberService;

    @BeforeEach
    void beforeEach() {
        AppConfig appConfig = new AppConfig();
        memberService = appConfig.memberService();
    }

    @AfterEach
    void afterEach() {
        AppConfig appConfig = new AppConfig();
        appConfig.memberRepository().clear();
    }

    @Test
    void join() {
        // given
        Member member = new Member(1L, "seongtki", Grade.VIP);

        // when
        memberService.join(member);
        Member finded = memberService.findMember(1L);
        System.out.println(finded   );
        // then
        Assertions.assertThat(member.getName()).isEqualTo(finded.getName());

    }

    @Test
    void findMember() {
        // given
        Member member = new Member(1L, "seongtki", Grade.VIP);

        // when
        memberService.join(member);
        Member finded = memberService.findMember(1L);

        // then
        Assertions.assertThat(member).isEqualTo(finded);
    }
}
