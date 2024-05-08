package me.staek.jdbc.service;

import me.staek.jdbc.BankAccount;
import me.staek.jdbc.repository.BankAccountRepositoryV3;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import java.sql.SQLException;

import static me.staek.jdbc.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 트랜잭션 - PlatformTransactionManager 를 이용해서 테스트.
 */
class BankAccountServiceV3_2Test {

    public static final String MEMBER_A = "memberA";
    public static final String MEMBER_B = "memberB";
    public static final String MEMBER_EX = "ex";

    private BankAccountRepositoryV3 accountRepository;
    private BankAccountServiceV3_2 accountService;

    @BeforeEach
    void before() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        accountRepository = new BankAccountRepositoryV3(dataSource);
        PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
        accountService = new BankAccountServiceV3_2(transactionManager, accountRepository);
    }

    @AfterEach
    void after() throws SQLException {
        accountRepository.delete(MEMBER_A);
        accountRepository.delete(MEMBER_B);
        accountRepository.delete(MEMBER_EX);
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
        accountService.accountTransfer(memberA.getAccountId(), memberB.getAccountId(), 2000);

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
        assertThatThrownBy(() -> accountService.accountTransfer(memberA.getAccountId(), memberEx.getAccountId(), 2000))
                .isInstanceOf(IllegalStateException.class);

        //then
        BankAccount findMemberA = accountRepository.findById(memberA.getAccountId());
        BankAccount findMemberB = accountRepository.findById(memberEx.getAccountId());
        assertThat(findMemberA.getMoney()).isEqualTo(10000);
        assertThat(findMemberB.getMoney()).isEqualTo(10000);
    }

}
