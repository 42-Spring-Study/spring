package hello.springtx.apply;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class InternalCallV1Test {
    @Autowired
    CallService callService;
    @Autowired
    private ApplicationContext applicationContext;
    @Test
    void printProxy () {
        log.info("callService class = {}", callService.getClass());
    }

    @Test
    void internalCall() {
        callService.internal();
    }

    @Test
    void externalCall() {
        callService.external();
    }

//    @EventListener(value = ApplicationReadyEvent.class)
//    @PostConstruct
    @Test
    public void listBeans() {
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        System.out.println("Registered Beans:");
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
    }

    @TestConfiguration
    static class InternalCallV1TestConfig {
        @Bean
        public CallService callService() {
            return new CallService();
        }
    }

    static class CallService {
        public void external(){
            log.info("call external");
            printTxInfo();
            internal();
        }

        @Transactional
        public void internal(){
            log.info("call internal");
            printTxInfo();
        }

        private void printTxInfo(){
            boolean actualTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active = {}", actualTransactionActive);

            boolean currentTransactionReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
            log.info("tx readOnly = {}", currentTransactionReadOnly);
        }
    }
}
