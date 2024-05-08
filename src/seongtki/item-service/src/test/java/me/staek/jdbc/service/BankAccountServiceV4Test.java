package me.staek.jdbc.service;

import lombok.extern.slf4j.Slf4j;
import me.staek.jdbc.BankAccount;
import me.staek.jdbc.repository.AccountRepository;
import me.staek.jdbc.repository.BankAccountRepositoryV4_1;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 예외 누수 문제 해결
 * SQLException 제거
 *
 * MemberRepository 인터페이스 의존
 */
@Slf4j
@SpringBootTest
class BankAccountServiceV4Test {

    public static final String MEMBER_A = "memberA";
    public static final String MEMBER_B = "memberB";
    public static final String MEMBER_EX = "ex";

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private BankAccountServiceV4 accountService;

    @TestConfiguration
    static class TestConfig {

        private final DataSource dataSource;

        public TestConfig(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Bean
        AccountRepository accountRepository() {
            return new BankAccountRepositoryV4_1(dataSource);
//            return new BankAccountRepositoryV4_2(dataSource);
//            return new BankAccountRepositoryV5(dataSource);
        }

        @Bean
        BankAccountServiceV4 accountService() {
            return new BankAccountServiceV4(accountRepository());
        }
    }

    @AfterEach
    void after() {
        accountRepository.delete(MEMBER_A);
        accountRepository.delete(MEMBER_B);
        accountRepository.delete(MEMBER_EX);
    }

    @Test
    void AopCheck() {
        log.info("memberService class={}", accountService.getClass());
        log.info("memberRepository class={}", accountRepository.getClass());
        assertThat(AopUtils.isAopProxy(accountService)).isTrue();
        assertThat(AopUtils.isAopProxy(accountRepository)).isFalse();
    }

    @Test
    @DisplayName("정상 이체")
    void accountTransfer() {
        //given
        BankAccount memberA = new BankAccount(MEMBER_A, 10000);
        BankAccount memberB = new BankAccount(MEMBER_B, 10000);
        accountRepository.save(memberA);
        accountRepository.save(memberB);

        //when
        accountService.accountTransfer(memberA.getAccountId(), memberB.getAccountId(), 2000);

        //then
        BankAccount findMemberA = accountRepository.findById(memberA.getAccountId());
        BankAccount findMemberB = accountRepository.findById(memberB.getAccountId());
        assertThat(findMemberA.getMoney()).isEqualTo(8000);
        assertThat(findMemberB.getMoney()).isEqualTo(12000);
    }

    @Test
    @DisplayName("이체중 예외 발생")
    void accountTransferEx() {
        BankAccount memberA = new BankAccount(MEMBER_A, 10000);
        BankAccount memberEx = new BankAccount(MEMBER_EX, 10000);
        accountRepository.save(memberA);
        accountRepository.save(memberEx);

        assertThatThrownBy(() -> accountService.accountTransfer(memberA.getAccountId(), memberEx.getAccountId(), 2000))
                .isInstanceOf(IllegalStateException.class);

        BankAccount findMemberA = accountRepository.findById(memberA.getAccountId());
        BankAccount findMemberB = accountRepository.findById(memberEx.getAccountId());
        assertThat(findMemberA.getMoney()).isEqualTo(10000);
        assertThat(findMemberB.getMoney()).isEqualTo(10000);
    }

}
