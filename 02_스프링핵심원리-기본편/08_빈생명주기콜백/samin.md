- 빈 생명주기 콜백 시작
    
    - **스프링 빈의 이벤트 라이프사이클**
        1. **스프링 컨테이너 생성**
        2. **스프링 빈 생성**
        3. **의존관계 주입**
        4. **초기화 콜백**
        5. **사용**
        6. **소멸전 콜백**
    - **초기화 콜백**: 빈이 생성되고, 빈의 의존관계 주입이 완료된 후 호출
    - **소멸전 콜백**: 빈이 소멸되기 직전에 호출
    - **스프링은 크게 3가지 방법으로 빈 생명주기 콜백을 지원한다.**
        - 인터페이스(InitializingBean, DisposableBean)
        - 설정 정보에 초기화 메서드, 종료 메서드 지정
        - @PostConstruct, @PreDestroy 애노테이션 지원
- **인터페이스** InitializingBean, DisposableBean
    
    InitializingBean 은 afterPropertiesSet() 메서드로 초기화를 지원한다.
    
    DisposableBean 은 destroy() 메서드로 소멸을 지원한다.
    
    ```java
     public class NetworkClient implements InitializingBean, DisposableBean {
    		...
    		@Override
        public void afterPropertiesSet() throws Exception {
    			connect();
    				call("초기화 연결 메시지"); 
    		}
    		
        @Override
        public void destroy() throws Exception {
            disConnect();
        }
     }
    ```
    
    - **초기화, 소멸 인터페이스 단점**
        - 이 인터페이스는 스프링 전용 인터페이스다. 해당 코드가 스프링 전용 인터페이스에 의존한다.
        - 초기화, 소멸 메서드의 이름을 변경할 수 없다.
        - 내가 코드를 고칠 수 없는 외부 라이브러리에 적용할 수 없다.
    - 인터페이스를 활용하는 방식은 초기 방식이고 이ㅈ는 거의 사용 하지 않는다.
- **빈 등록 초기화**, **소멸 메서드 지정**
    
    설정 정보에 `@Bean(initMethod = "init", destroyMethod = "close")` 처럼 초기화, 소멸 메서드를 지정할 수 있다.
    
    ```java
    @Configuration
     static class LifeCycleConfig {
         @Bean(initMethod = "init", destroyMethod = "close")
         public NetworkClient networkClient() {
             NetworkClient networkClient = new NetworkClient();
             networkClient.setUrl("<http://hello-spring.dev>");
             return networkClient;
    		} 
    }
    ```
    
- **애노테이션** @PostConstruct, @PreDestroy
    
    - 최신 스프링에서 권장하는 방식
    - 스프링에 종속적인 기술이 아니다
    - 컴포넌트 스캔과 잘 어울린다.
    - 외부 라이이브러리에는 적용하지 못한다
    - 외부 라이브러리를 초기화, 종료 해야 하면 @Bean의 기능을 사용
    
    ```java
    public class NetworkClient {
    	... 
    	@PostConstruct
    	public void init() {
    			System.out.println("NetworkClient.init"); connect();
    			call("초기화 연결 메시지");
    	}
    	 @PreDestroy
    	 public void close() {
    	     System.out.println("NetworkClient.close");
    	     disConnect();
    	 }
    }
    ```