package hello.core.sec06;


import hello.core.sec06.member.MemberRepository;
import hello.core.sec06.member.MemeryMemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        excludeFilters = @ComponentScan.Filter
                (type = FilterType.ANNOTATION, classes = Configuration.class)
        , basePackages = "hello.core.sec06.member"
)
public class AutoAppConfig {
//    @Bean(name = "memeryMemberRepository")
//    public MemberRepository memberRepository() {
//        return new MemeryMemberRepository();
//    }
}
