package me.staek.jdbc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.staek.jdbc.BankAccount;
import me.staek.jdbc.repository.BankAccountRepositoryV2;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 계좌이체 기능 구현
 * - 트랜잭션 로직 안에서 서비스로직을 진행하도록 변경한다.
 */
@Slf4j
@RequiredArgsConstructor
public class BankAccountServiceV2 {

    private final DataSource dataSource;
    private final BankAccountRepositoryV2 accountRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        Connection con = dataSource.getConnection();
        try {
            con.setAutoCommit(false);
            bizLogic(con, fromId, toId, money);
            con.commit();
        } catch (Exception e) {
            con.rollback();
            throw new IllegalStateException(e);
        } finally {
            release(con);
        }

    }

    private void bizLogic(Connection con, String fromId, String toId, int money) throws SQLException {
        BankAccount fromMember = accountRepository.findById(con, fromId);
        BankAccount toMember = accountRepository.findById(con, toId);

        accountRepository.update(con, fromId, fromMember.getMoney() - money);
        validation(toMember);
        accountRepository.update(con, toId, toMember.getMoney() + money);
    }

    private void validation(BankAccount toMember) {
        if (toMember.getAccountId().equals("ex")) {
            throw new IllegalStateException("이체중 예외 발생");
        }
    }

    private void release(Connection con) {
        if (con != null) {
            try {
                con.setAutoCommit(true); // 커넥션 풀에 원상태로 돌려준다.
                con.close();
            } catch (Exception e) {
                log.info("error", e);
            }
        }
    }
}
