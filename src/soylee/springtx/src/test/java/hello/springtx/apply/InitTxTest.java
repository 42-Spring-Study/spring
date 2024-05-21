package hello.springtx.apply;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@SpringBootTest
public class InitTxTest {
    @Autowired
    Hello hello;

    @Test
    void test(){
        //초기화코드는 스프링이 초기화 시점에 호출한다.
    }
    @TestConfiguration
    static class InitTxTestConfig {
        @Bean
        Hello hello () {
            return new Hello();
        }
    }

    @Slf4j
    static class Hello {

        @Transactional
        @PostConstruct //NOTE: 트랜젝션 적용 안된다.
        public void initV1() {
            boolean active = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("@PostConstruct: tx active = {}", active);
        }

        @Transactional
        @EventListener(value = ApplicationReadyEvent.class) //NOTE: 트랜잭션 적용됨.
        public void initV2(){
            boolean active = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("ApplicationReadyEvent: tx active = {}", active);
        }
    }
}
