package me.staek.jdbc.service;

import me.staek.jdbc.BankAccount;
import me.staek.jdbc.repository.BankAccountRepositoryV1;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;

import static me.staek.jdbc.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 계좌이체 테스트
 * - 실패테스트에 대해 예외처리를 확인
 * - autocommit에 의해 트랜잭션 관리가 안됨을 확인.
 */
class BankAccountServiceV1Test {

    public static final String MEMBER_A = "memberA";
    public static final String MEMBER_B = "memberB";
    public static final String MEMBER_EX = "ex";

    private BankAccountRepositoryV1 accountRepository;
    private BankAccountServiceV1 accountService;

    @BeforeEach
    void before() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        accountRepository = new BankAccountRepositoryV1(dataSource);
        accountService = new BankAccountServiceV1(accountRepository);
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
        BankAccount memberA = new BankAccount(MEMBER_A, 10000);
        BankAccount memberEx = new BankAccount(MEMBER_EX, 10000);
        accountRepository.save(memberA);
        accountRepository.save(memberEx);

        assertThatThrownBy(() -> accountService.accountTransfer(memberA.getAccountId(), memberEx.getAccountId(), 2000))
                                            .isInstanceOf(IllegalStateException.class);

        BankAccount findMemberA = accountRepository.findById(memberA.getAccountId());
        BankAccount findMemberB = accountRepository.findById(memberEx.getAccountId());
        assertThat(findMemberA.getMoney()).isEqualTo(8000);
        assertThat(findMemberB.getMoney()).isEqualTo(10000);
    }
}
