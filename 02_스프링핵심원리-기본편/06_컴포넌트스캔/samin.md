# 컴포넌트 스캔

- 컴포넌트 스캔과 의존관계 자동 주입 시작하기
    
    기존에는 @bean 또는 xml로 직접 스프리빈을 컨테이너에 등록하였다.
    
    하지만 @ComponentScan 을 설정 정보에 붙여주면 직접 빈을 등록하지 않아도 @Component 이 붙여져있는 클래스를 확인하여 빈을 자동으로 등록해 준다.
    
    ```java
    @Configuration
    @ComponentScan(
     excludeFilters = @Filter(type = FilterType.ANNOTATION, classes =
    Configuration.class))
    public class AutoAppConfig {
     
    }
    ```
    
    @ComponentScan
    
    - @ComponentScan 은 @Component 가 붙은 모든 클래스를 스프링 빈으로 등록한다.
    - 이때 스프링 빈의 기본 이름은 클래스명을 사용하되 맨 앞글자만 소문자를 사용한다.
        - 빈 이름 기본 전략: MemberServiceImpl 클래스 memberServiceImpl
        - 빈 이름 직접 지정: 만약 스프링 빈의 이름을 직접 지정하고 싶으면@Component("memberService2") 이런식으로 이름을 부여하면 된다
    
    @Component
    
    @Autowired
    
    - 생성자에 @Autowired 를 지정하면, 스프링 컨테이너가 자동으로 해당 스프링 빈을 찾아서 주입한다.
    - 기본 조회 전략은 타입이 같은 빈을 찾아서 주입한다.
- 탐색 위치와 기본 스캔 대상
    
    탐색할 패키지의 시작 위치 지정
    
    - @ComponentScan 의 스캔 시작 위치를 지정할수 있다.
        
    - 만약 지정하지 않으면 @ComponentScan 이 붙은 설정 정보 클래스의 패키지가 시작 위치가 된다
        
    - 권장하는 방법
        
    - 패키지 위치를 지정하지 않고, 설정 정보 클래스의 위치를 프로젝트 최상단에 두는 것. 최근 스프링 부트도 이 방법을 기본으로 제공한다
        
    - 스프링 부트를 사용하면 스프링 부트의 대표 시작 정보인 @SpringBootApplication 를 이 프로젝트 시작 루트 위치에 두는 것이 관례이다. (그리고 이 설정안에 바로 @ComponentScan 이 들어있다)
        
    
    컴포넌트 스캔 기본 대상
    
    - 컴포넌트 스캔은 @Component 뿐만 아니라 다음과 내용도 추가로 대상에 포함한다.
        - @Component : 컴포넌트 스캔에서 사용
        - @Controller : 스프링 MVC 컨트롤러에서 사용
            - 스프링 MVC 컨트롤러로 인식
        - @Service : 스프링 비즈니스 로직에서 사용
            - 사실 @Service 는 특별한 처리를 하지 않는다. 개발자들이 핵심 비즈니스 로직이 여기에 있겠구나 라고 비즈니스 계층을 인식하는데 도움이 된다.
        - @Repository : 스프링 데이터 접근 계층에서 사용
            - 스프링 데이터 접근 계층으로 인식하고, 데이터 계층의 예외를 스프링 예외로 변환해준다.
        - @Configuration : 스프링 설정 정보에서 사용
            - 스프링 설정 정보로 인식하고, 스프링 빈이 싱글톤을 유지하도록 추가 처리를 한다
- 필터
    
    필터를 사용하여 컴포넌트스캔에 추가할대상, 제외할 대상을 각각 지정할수가 있다.
    
    includeFilters : 컴포넌트 스캔 대상을 추가로 지정한다. excludeFilters : 컴포넌트 스캔에서 제외할 대상을 지정한다.
    
    컴포넌트 스캔 대상에 추가할 어노테이
    
    @Target(ElementType.TYPE) @Retention(RetentionPolicy.RUNTIME) @Documented @interface
    
    FilterType **옵션** FilterType은 5가지 옵션이 있다.
    
    ANNOTATION
    
    - 기본값, 애노테이션을 인식해서 동작한다.
    
    ASSIGNABLE_TYPE
    
    - 지정한 타입과 자식 타입을 인식해서 동작한다.
    
    ASPECTJ
    
    - AspectJ 패턴 사용
    
    REGEX
    
    - 정규 표현식
    
    CUSTOM
    
    - `TypeFilter` 이라는 인터페이스를 구현해서 처리
    
    @Component 면 충분하기 때문에, includeFilters 를 사용할 일은 거의 없다. excludeFilters 는 여러가지 이유로 간혹 사용할 때가 있지만 많지는 않다. 스프링부트에서 제공하는 기본컴포넌트들을 잘활용하는것을 권장한다.
    
- **중복 등록과 충돌**
    
    컴포넌트 스캔에서 같은 빈 이름을 등록하는 두가지 상황이 있다.
    
    1. 자동빈등록vs자동빈등록
        
        `ConflictingBeanDefinitionException` 예외 발생
        
    2. 수동빈등록vs자동빈등록
        
        이 경우 수동 빈 등록이 우선권을 가진다.
        
        **수동 빈 등록시 남는 로그**
        
        `Overriding bean definition for bean 'memoryMemberRepository' with a different definition: replacing`
        
        **수동 빈 등록, 자동 빈 등록 오류시 스프링 부트 에러**
        
        `Consider renaming one of the beans or enabling overriding by setting spring.main.allow-bean-definition-overriding=true`