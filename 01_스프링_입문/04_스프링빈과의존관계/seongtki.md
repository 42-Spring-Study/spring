# 04. 스프링 빈과 의존관계



빈 컨테이너 라고 불리기도하고 스프링 3대 요소중의 하나일 정도로 중요한 개념이다.
스프링의 DI를 이해하기 위해 리플렉션, 스프링 컨테이너 생성주기 등을 자세히 알아야 하지만

해당 섹션에서는 간단한 MVC에서의 의존관계 설정하는 방법, @Configuration, @Bean 을 이용한 빈 생성 방법을 학습한다.





## 1) 컴포넌트 스캔과 자동 의존관계 설정

회원 컨트롤러가 회원서비스와 회원 리포지토리를 사용할 수 있게 의존관계를 준비하자.



##### 회원 컨트롤러에 의존관계 추가

생성자에 @Autowired 가 있으면 스프링이 연관된 객체를 스프링 컨테이너에서 찾아서 넣어준다. 이렇게
객체 의존관계를 외부에서 넣어주는 것을 DI (Dependency Injection), 의존성 주입이라 한다.
이전 테스트에서는 개발자가 직접 주입했고, 여기서는 @Autowired에 의해 스프링이 주입해준다.



![스크린샷 2024-03-17 오후 1.15.24](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2024-03-17 오후 1.15.24.png)

컴파일 타임에 `@Autowired` 대상 객체가 빈 등록 되어있는 지 검사하고 없다면 위 캡처처럼 컴파일에러가 발생한다.



### 자동 의존관계 설정

`@Autowired`대상 객체를 모두 빈 등록하자.

`@Controller` `@Service` `@Repository` 는 모두 `@Component` 를 포함하고 있어 컴파일 시 빈 등록된다.

- `@Component` 애노테이션이 있으면 스프링 빈으로 자동 등록된다.

~~~java
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    ...
~~~

~~~java
@Repository
public class MemoryMemberRepository implements MemberRepository {
...
~~~





## 2) 자바 코드로 직접 스프링 빈 등록하기



회원 서비스와 회원 리포지토리의 @Service, @Repository, @Autowired 애노테이션을 제거하고 진행 한다.

~~~java
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
~~~

- XML로 설정하는 방식도 있지만 최근에는 잘 사용하지 않으므로 생략한다.
  - 빈등록 개수가 많아지면 관리가 매우 어렵다.(가독성 등 문제)
- DI에는 필드 주입, setter 주입, 생성자 주입 이렇게 3가지 방법이 있다.
  의존관계가 실행중에 동적으 로 변하는 경우는 거의 없으므로 생성자 주입을 권장한다.
  - 필드주입 : 메서드형태가 아니기 때문에 추가기능 혹은 생성자변경로직을 작성할 수 없다는 단점 존재.
  - setter 주입 : 런타임에 가변으로 리소스가 변경되어 의도치 않은 동작을 유발할 수 있다.

- 실무에서는 주로 정형화된 컨트롤러, 서비스, 리포지토리 같은 코드는 컴포넌트 스캔을 사용한다.
  한편, 정형화 되지 않거나, 상황에 따라 구현 클래스를 변경해야 하면 설정을 통해 스프링 빈으로 등록한다.

- @Autowired 를 통한 DI는 helloController , memberService 등과 같이 스프링이 관리하 는 객체에서만 동작한다. 스프링 빈으로 등록하지 않고 내가 직접 생성한 객체에서는 동작하지 않는다.







## 마치며

Spring DI 는 스프링 3대요소 중 하나인만큼 중요하도 방대하다.

스프링 핵심 원리 -기본편에서 조금더 자세하게 학습할 예정이다.





















