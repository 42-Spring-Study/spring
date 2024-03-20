# 4. 스프링 빈과 의존관계

스프링 빈을 등록하는 방법에는 다음 2가지 방법이 있다.

# 컴포넌트 스캔과 자동 의존관계 설정

## 컴포넌트 스캔 원리

- `@Component` 이 `@SpringBootApplication`이 존재하는 하위 패키지에 존재하면스프링빈으로 자동 등록
- 생성자에 `@Autowired` 붙어 있으면, 생성 시점에 컨테이너에서 해당 스프링빈을 찾아 주입한다.
- `@Componet` 를 포함하는 다음 어노테이션도 스프링빈으로 자동 등록된다.
    - `@Controller`, `@Service`, `@Repository`

→ 보통 정형화된 패턴에 사용된다.

![스크린샷 2024-03-20 오전 12.52.36.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2e635c72-05ea-4289-aeec-6a010538da4f/d45308f4-c8c4-4c61-afab-0155b64e3dff/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA_2024-03-20_%E1%84%8B%E1%85%A9%E1%84%8C%E1%85%A5%E1%86%AB_12.52.36.png)

 참고로, 스프링 컨테이너에 스프링 빈 등록 시, 유일하게 하나만 등록해서 공유해 사용한다. 이를 싱글톤으로 등록한다고 한다. 따라서 같은 스프링 빈이면 모두 같은 인스턴스이다.

```java
@Controller
public class MemberController {
    private MemberService memberService;

    @Autowired
    MemberController(MemberService memberService){
        this.memberService = memberService;
    }
}
```

```java
public class MemberService {
    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }
}
```

```java
@Repository
public class MemoryMemberRepository implements MemberRepository{...}
```

# 자바 코드로 직접 스프링 빈 등록하기

```java
package hello.hellospring;

import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import hello.hellospring.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {
    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }
}
```

**DI 주입 방법**

1. 필드 주입: `@Autowired private MemberService memberService`
2. setter 주입: 
`@Autowird public void setMemberService(MemberService ms){this.ms = ms;}` 
3. 생성자 주입 (권장): 위 코드가 이 방식으로 구현되었다.

실무에서는 정형화됨 → 컴포넌트 스캔
상황에 따라 구현체 변경 필요 → 스프링 빈으로 등록 → 변경 시 다른 코드 수정 필요 없다

@Autowired를 통한 DI는 스프링이 관리하는 객체에서만 동작한다.