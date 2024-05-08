package me.staek.jdbc.service;

import me.staek.jdbc.BankAccount;
import me.staek.jdbc.repository.BankAccountRepositoryV2;
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
 * - 계좌이체에 대해 트랜잭션 동작을 확인
 */
class BankAccountServiceV2Test {

    public static final String MEMBER_A = "memberA";
    public static final String MEMBER_B = "memberB";
    public static final String MEMBER_EX = "ex";

    private BankAccountRepositoryV2 memberRepository;
    private BankAccountServiceV2 memberService;

    @BeforeEach
    void before() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        memberRepository = new BankAccountRepositoryV2(dataSource);
        memberService = new BankAccountServiceV2(dataSource, memberRepository);
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
        BankAccount memberA = new BankAccount(MEMBER_A, 10000);
        BankAccount memberB = new BankAccount(MEMBER_B, 10000);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        memberService.accountTransfer(memberA.getAccountId(), memberB.getAccountId(), 2000);

        BankAccount findMemberA = memberRepository.findById(memberA.getAccountId());
        BankAccount findMemberB = memberRepository.findById(memberB.getAccountId());
        assertThat(findMemberA.getMoney()).isEqualTo(8000);
        assertThat(findMemberB.getMoney()).isEqualTo(12000);
    }

    @Test
    @DisplayName("이체중 예외 발생")
    void accountTransferEx() throws SQLException {
        BankAccount memberA = new BankAccount(MEMBER_A, 10000);
        BankAccount memberEx = new BankAccount(MEMBER_EX, 10000);
        memberRepository.save(memberA);
        memberRepository.save(memberEx);

        assertThatThrownBy(() -> memberService.accountTransfer(memberA.getAccountId(), memberEx.getAccountId(), 2000))
                .isInstanceOf(IllegalStateException.class);

        BankAccount findMemberA = memberRepository.findById(memberA.getAccountId());
        BankAccount findMemberB = memberRepository.findById(memberEx.getAccountId());
        assertThat(findMemberA.getMoney()).isEqualTo(10000);
        assertThat(findMemberB.getMoney()).isEqualTo(10000);
    }

}
