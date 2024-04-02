package hello.core.sec07;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        excludeFilters = @ComponentScan.Filter
                (type = FilterType.ANNOTATION, classes = Configuration.class)
        , basePackages = "hello.core.sec07"
)
public class AutoAppConfig {
//    @Bean(name = "memeryMemberRepository")
//    public MemberRepository memberRepository() {
//        return new MemeryMemberRepository();
//    }
}
