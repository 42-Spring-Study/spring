# 의존관계 자동 주입

- 다양한 의존관계 주인 방법
    
    - 생성자 주입
        
        - 생성자에 @Autowired
        - 생성자 호출시 딱 한번만 호출하는것이 보장된다.
        - 불변, 필수 의존관계에 사용
        
        제약, 한계가 있는 설계가 좋다, 좋은 습관
        
    - 수정자 주입(setter 주입)
        
        - setter에 @Autowired
        - **선택, 변경** 가능성이 있는 의존관계에 사용
        - 자바빈 프로퍼티
        
    - 필드
        
        - 코드가 간결해 진다는 장점이 있지만 순수자바로 테스트가 불가능하다
            - 필드주입을 사용한 코드를 테스트 하기 위해서는 스프링 컨테이너가 필요하다. DI 프레임워크가 없으면 아무것도 할 수 없다.
        - 사용하지 말자.
        - 사용해도 될떄
            - 어플리케이션과 관계없는 테스트
            - 스프링 설정에 필요한 @Configuration 에 사용가능
    - 일반 메서드 주입
        
        - 일반 메서드에 @Autowired
        - 한번에 여러 필드를 주입 받을수 있다,
        - 잘 사용하지 않는다.
- 옵션처리
    
    - 주입할 스프링 빈이 없어도 동작해야 할 경우
        - 옵션으로 사용할때
    - `@Autowired(required=false)` : 자동 주입할 대상이 없으면 수정 메서드 자체가 호출 안됨
    - `@Nullable`: 자동 주입할 대상이 없으면 null이 입력된다.
    - `Optional<>` : 자동 주입할 대상이 없으면 `Optional.empty` 가 입력된다. (값이 있을수도 있고 없을수도 있다는 상태 자바8에서 제공)
    - 참고: @Nullable, Optional은 스프링 전반에 걸쳐서 지원된다. 예를 들어서 생성자 자동 주입에서 특정 필드에 만 사용해도 된다
- **생성자 주입을 선택해라**!
    
    - 스프링을 포함한 DI 프레임워크 대부분이 생성자 주입을 권장하는 이유
        - 불변
            - 생성자 주입은 객체를 생성할 때 딱 1번만 호출되므로 이후에 호출되는 일이 없다. 따라서 불변하게 설계할 수 있다.
        - 누락
            - 생성자 주입을 사용하면 다음처럼 주입 데이터를 누락 했을 때 ****컴파일 오류****가 발생한다.
        - **final 키워드**
            - 생성자 주입을 사용하면 필드에 `final` 키워드를 사용할 수 있다. 그래서 생성자에서 혹시라도 값이 설정되지 않는 오류를 컴파일 시점에 막아준다.
    - 프레임워크에 의존하지 않고, 순수한 자바 언어의 특징을 잘 살리는 방법
    - 기본으로 생성자 주입을 사용하고, 필수 값이 아닌 경우에는 수정자 주입 방식을 옵션으로 부여하면 된다. 생성자주입과 수정자 주입을 동시에 사용할 수 있다.
    - 필드 주입은 사용하지 않는것이 좋다.
- 롬복과 최신 트랜드
    
    - @Getter @Setter
    - @RequiredArgsConstructor 생성자를 만들어줌
- 조회 빈이 2개 이상 - 문제
    
    ```java
    @Component
    public class FixDiscountPolicy implements DiscountPolicy {}
    @Component
    public class RateDiscountPolicy implements DiscountPolicy {}
    
    @Autowired
    private DiscountPolicy discountPolicy
    ```
    
- @Autowired **필드 명**, @Qualifier, @Primary
    
    조회빈이 2개 이상일떄의 문제
    
    - @Autowired **필드 명 매칭**
        
        - 타입매칭의결과가2개이상일때필드명,파라미터명으로빈이름매칭시켜준다
        
        ```java
         @Autowired
         private DiscountPolicy rateDiscountPolicy //타입 명이 주입하려는 타입명과 동일함.
        ```
        
    - @Qualifier **사용**
        
        ```java
         @Component
         @Qualifier("mainDiscountPolicy")
        public class RateDiscountPolicy implements DiscountPolicy {}
        
        @Autowired
        public OrderServiceImpl(MemberRepository memberRepository,
               @Qualifier("mainDiscountPolicy") DiscountPolicy discountPolicy) {
           this.memberRepository = memberRepository;
           this.discountPolicy = discountPolicy;
        }
        ```
        
        - @Qualifier는 추가 구분자를 붙여주는 방법이다. 주입시 추가적인 방법을 제공하는 것이지 빈 이름을 변경하는 것은 아니다.
        - 문제는 햇갈린다
        - `@Qualifier` 는 `@Qualifier` 를 찾 는 용도로만 사용하는게 명확하고 좋다.
    - @Primary **사용**
        
        - `@Primary` 는 우선순위를 정하는 방법이다. @Autowired 시에 여러 빈이 매칭되면 `@Primary` 가 우선권을 가진다.
        - @Qualifier 가 @Primary 보다 우선순위가 더 높다
- **애노테이션 직접 만들기**
    
    여러 애노테이션을 모아서 사용하는 기능은 스프링이 지원해주는 기능 이다.
    
    자바에서 제공하는 기능이 아니다.
    
    ```java
    package hello.core.annotataion;
     import org.springframework.beans.factory.annotation.Qualifier;
     import java.lang.annotation.*;
     
     @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
     ElementType.TYPE, ElementType.ANNOTATION_TYPE})
     @Retention(RetentionPolicy.RUNTIME)
     @Documented
     @Qualifier("mainDiscountPolicy")
     public @interface MainDiscountPolicy {
    }
    ```
    
- **조회한 빈이 모두 필요할 때**, List, Map
    
    ```java
    private final Map<String, DiscountPolicy> policyMap;
    private final List<DiscountPolicy> policies;
    
    public DiscountService(Map<String, DiscountPolicy> policyMap,
    List<DiscountPolicy> policies) {
          this.policyMap = policyMap;
          this.policies = policies;
     }
    ```
    
- **자동**, **수동의 올바른 실무 운영 기준**
    
    애플리케이션은 크게 업무 로직과 기술 지원 로직으로 나눌 수 있다.
    
    **업무 로직 빈:** 웹을 지원하는 컨트롤러, 핵심 비즈니스 로직이 있는 서비스, 데이터 계층의 로직을 처리하는 리포지토리등이 모두 업무 로직이다. 보통 비즈니스 요구사항을 개발할 때 추가되거나 변경된다.
    
    **기술 지원 빈:** 기술적인 문제나 공통 관심사(AOP)를 처리할 때 주로 사용된다. 데이터베이스 연결이나, 공통 로그 처리 처럼 업무 로직을 지원하기 위한 하부 기술이나 공통 기술들이다.
    
    업무로직 빈 = 자동
    
    기술 지원 빈 = 수동
    
    - **애플리케이션에 광범위하게 영향을 미치는 기술 지원 객체는 수동 빈으로 등록해서 설정 정보에 바로 나타나게 하는것이 유지보수 하기 좋다.**
    - **비즈니스 로직 중에서 다형성을 적극 활용할 때**