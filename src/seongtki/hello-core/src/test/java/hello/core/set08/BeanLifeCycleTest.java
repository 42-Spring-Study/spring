package hello.core.set08;

import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 빈 생명주기 중 시작과 종료 시점을 직접 호출할 수 있는 방법 4가지를 장단점과 함께 작성한다.
 *
 * 1) NetworkClient - 사용안함
 * 2) NetworkClient2 - InitializingBean, DisposableBean
 * 3) NetworkClient3 - @Bean 에서 초기화, 소멸 메서드 지정
 * 4) NetworkClient4 - @PostConstruct, @PreDestroy
 *
 */
public class BeanLifeCycleTest {
    @Test
    public void lifeCycleTest() {
        ConfigurableApplicationContext ac = new
                AnnotationConfigApplicationContext(LifeCycleConfig.class);
        NetworkClient3 client = ac.getBean(NetworkClient3.class);
        ac.close(); //스프링 컨테이너를 종료, ConfigurableApplicationContext 필요
    }


    // 예제 1,2,4 에서 사용
//    @Configuration
//    static class LifeCycleConfig {
//        @Bean
//        public NetworkClient4 networkClient() {
//            NetworkClient4 networkClient = new NetworkClient4();
//            networkClient.setUrl("http://hello-spring.dev");
//            return networkClient;
//        }
//    }

    // 예제 3에서 사용
    @Configuration
    static class LifeCycleConfig {
        @Bean(initMethod = "init", destroyMethod = "close")
        public NetworkClient3 networkClient() {
            NetworkClient3 networkClient = new NetworkClient3();
            networkClient.setUrl("http://hello-spring.dev");
            return networkClient;
        }
    }
}
