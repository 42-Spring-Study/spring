package me.staek.jdbc.service;

import lombok.extern.slf4j.Slf4j;
import me.staek.jdbc.BankAccount;
import me.staek.jdbc.repository.BankAccountRepositoryV3;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

import static me.staek.jdbc.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 선언적 트랜잭션 기반 계좌이체 테스트
 * - @Transactional AOP
 *
 * 스프링 AOP를 적용하려면 스프링 컨테이너가 필요하다.
 */
@Slf4j
@SpringBootTest
class BankAccountServiceV3_3Test {

    public static final String MEMBER_A = "memberA";
    public static final String MEMBER_B = "memberB";
    public static final String MEMBER_EX = "ex";

    @Autowired
    private BankAccountRepositoryV3 accountRepository;
    @Autowired
    private BankAccountServiceV3_3 memberService;

    /**
     * 스프링이 제공하는 트랜잭션 AOP는 스프링 빈에 등록된 트랜잭션 매니저를 찾아서 사용하기 때문에
     * 트랜잭션 매니저를 스프링 빈으로 등록해두어야 한다.
     */
    @TestConfiguration
    static class TestConfig {
        @Bean
        DataSource dataSource() {
            return new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        }

        @Bean
        PlatformTransactionManager transactionManager() {
            return new DataSourceTransactionManager(dataSource());
        }

        @Bean
        BankAccountRepositoryV3 memberRepositoryV3() {
            return new BankAccountRepositoryV3(dataSource());
        }

        @Bean
        BankAccountServiceV3_3 memberServiceV3_3() {
            return new BankAccountServiceV3_3(memberRepositoryV3());
        }
    }

    @AfterEach
    void after() throws SQLException {
        accountRepository.delete(MEMBER_A);
        accountRepository.delete(MEMBER_B);
        accountRepository.delete(MEMBER_EX);
    }

    /**
     * @Transactional 이 작성된 memberService의 클래스는 BankAccountServiceV3_3$$SpringCGLIB$$0 처럼 CGLIB가 생성한다.
     */
    @Test
    void AopCheck() {
        log.info("memberService class={}", memberService.getClass());
        log.info("memberRepository class={}", accountRepository.getClass());
        assertThat(AopUtils.isAopProxy(memberService)).isTrue();
        assertThat(AopUtils.isAopProxy(accountRepository)).isFalse();
    }

    @Test
    @DisplayName("정상 이체")
    void accountTransfer() throws SQLException {
        //given
        BankAccount memberA = new BankAccount(MEMBER_A, 10000);
        BankAccount memberB = new BankAccount(MEMBER_B, 10000);
        accountRepository.save(memberA);
        accountRepository.save(memberB);

        //when
        memberService.accountTransfer(memberA.getAccountId(), memberB.getAccountId(), 2000);

        //then
        BankAccount findMemberA = accountRepository.findById(memberA.getAccountId());
        BankAccount findMemberB = accountRepository.findById(memberB.getAccountId());
        assertThat(findMemberA.getMoney()).isEqualTo(8000);
        assertThat(findMemberB.getMoney()).isEqualTo(12000);
    }

    @Test
    @DisplayName("이체중 예외 발생")
    void accountTransferEx() throws SQLException {
        //given
        BankAccount memberA = new BankAccount(MEMBER_A, 10000);
        BankAccount memberEx = new BankAccount(MEMBER_EX, 10000);
        accountRepository.save(memberA);
        accountRepository.save(memberEx);

        //when
        assertThatThrownBy(() -> memberService.accountTransfer(memberA.getAccountId(), memberEx.getAccountId(), 2000))
                .isInstanceOf(IllegalStateException.class);

        //then
        BankAccount findMemberA = accountRepository.findById(memberA.getAccountId());
        BankAccount findMemberB = accountRepository.findById(memberEx.getAccountId());
        assertThat(findMemberA.getMoney()).isEqualTo(10000);
        assertThat(findMemberB.getMoney()).isEqualTo(10000);
    }

}
