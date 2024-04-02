package hello.core.sec09;


import hello.core.sec09.member.MemberRepository;
import hello.core.sec09.member.MemeryMemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

//@Configuration
@ComponentScan(
        excludeFilters = @ComponentScan.Filter
                (type = FilterType.ANNOTATION, classes = Configuration.class)
        , basePackages = "hello.core.sec09"
)
public class AutoAppConfig {
    @Bean(name = "memeryMemberRepository222")
    public MemberRepository memberRepository() {
        return new MemeryMemberRepository();
    }
}
