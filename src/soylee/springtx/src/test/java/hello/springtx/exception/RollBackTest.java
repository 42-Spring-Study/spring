package hello.springtx.exception;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
public class RollBackTest {
    @Autowired
    RollBackService rollBackService;

    @Test
    void uncheckedTest() {
        assertThatThrownBy(() -> rollBackService.uncheckedException())
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void checkedTest() {
        assertThatThrownBy(() -> rollBackService.checkedException())
                .isInstanceOf(MyException.class);
    }

    @Test
    void rollBackFor() {
        assertThatThrownBy(() -> rollBackService.rollbackFor())
                .isInstanceOf(MyException.class);
    }

    @TestConfiguration
    static class RollBackTestConfig {
        @Bean
        RollBackService rollBackService() {
            return new RollBackService();
        }
    }

    static class RollBackService {
        //런테임 예외 발생
        @Transactional
        public void uncheckedException() {
            log.info("uncheckedException");
            throw new RuntimeException();
        }

        //체크 예외 발생
        @Transactional
        public void checkedException() throws MyException {
            log.info("checkedException");
            throw new MyException();
        }

        //체크예외 rollbackFor 지정
        @Transactional(rollbackFor = MyException.class)
        public void rollbackFor() throws MyException {
            log.info("rollbackFor");
            throw new MyException();
        }
    }

    static class MyException extends Exception {

    }
}
