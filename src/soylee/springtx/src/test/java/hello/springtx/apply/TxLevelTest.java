package hello.springtx.apply;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@SpringBootTest
@Slf4j
public class TxLevelTest {
    @Autowired
    LevelService levelService;

    @Test
    @DisplayName("트랜젝션 우선순위 테스트")
    public void orderTest(){
        levelService.read();
        levelService.write();
    }

    @TestConfiguration
    static class TxLevelTestConfig {
        @Bean
        LevelService levelService(){
            return new LevelService();
        }
    }

    @Transactional(readOnly = true)
    @Slf4j
    static class LevelService {

        public void read() {
            log.info("call read");
            printTxInfo();
        }

//        @Transactional(readOnly = false)
        @Transactional
        public void write() {
            log.info("call write");
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
