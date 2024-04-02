package hello.core.step3.member;

import hello.core.step1.member.MemberRepository;
import hello.core.step3.AppConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

class MemberServiceImplTest {

    private MemberService memberService;

    @BeforeEach
    void beforeEach() {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        memberService = context.getBean("memberService", MemberService.class);
    }

    @AfterEach
    void afterEach() {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        MemeryMemberRepository repository = context.getBean("memberRepository", MemeryMemberRepository.class);
        repository.clear();
    }

    @Test
    void join() {
        // given
        Member member = new Member(1L, "seongtki", Grade.VIP);

        // when
        memberService.join(member);
        Member finded = memberService.findMember(1L);

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
