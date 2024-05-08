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
 * 트랜잭션 - 트랜잭션 매니저를 이용해서 계죄이체 테스트
 */
class BankAccountServiceV3_1Test {

    public static final String MEMBER_A = "memberA";
    public static final String MEMBER_B = "memberB";
    public static final String MEMBER_EX = "ex";

    private BankAccountRepositoryV3 memberRepository;
    private BankAccountServiceV3_1 memberService;

    @BeforeEach
    void before() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

        PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
        memberRepository = new BankAccountRepositoryV3(dataSource);
        memberService = new BankAccountServiceV3_1(transactionManager, memberRepository);
    }

    @AfterEach
    void after() throws SQLException {
        memberRepository.delete(MEMBER_A);
        memberRepository.delete(MEMBER_B);
        memberRepository.delete(MEMBER_EX);
    }

    @Test
    @DisplayName("정상 이체")
    void accountTransfer() throws SQLException {
        //given
        BankAccount memberA = new BankAccount(MEMBER_A, 10000);
        BankAccount memberB = new BankAccount(MEMBER_B, 10000);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        //when
        memberService.accountTransfer(memberA.getAccountId(), memberB.getAccountId(), 2000);

        //then
        BankAccount findMemberA = memberRepository.findById(memberA.getAccountId());
        BankAccount findMemberB = memberRepository.findById(memberB.getAccountId());
        assertThat(findMemberA.getMoney()).isEqualTo(8000);
        assertThat(findMemberB.getMoney()).isEqualTo(12000);
    }

    @Test
    @DisplayName("이체중 예외 발생")
    void accountTransferEx() throws SQLException {
        //given
        BankAccount memberA = new BankAccount(MEMBER_A, 10000);
        BankAccount memberEx = new BankAccount(MEMBER_EX, 10000);
        memberRepository.save(memberA);
        memberRepository.save(memberEx);

        //when
        assertThatThrownBy(() -> memberService.accountTransfer(memberA.getAccountId(), memberEx.getAccountId(), 2000))
                .isInstanceOf(IllegalStateException.class);

        //then
        BankAccount findMemberA = memberRepository.findById(memberA.getAccountId());
        BankAccount findMemberB = memberRepository.findById(memberEx.getAccountId());
        assertThat(findMemberA.getMoney()).isEqualTo(10000);
        assertThat(findMemberB.getMoney()).isEqualTo(10000);
    }

}
