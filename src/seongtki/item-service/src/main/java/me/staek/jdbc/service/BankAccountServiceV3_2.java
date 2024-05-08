package me.staek.jdbc.service;

import lombok.extern.slf4j.Slf4j;
import me.staek.jdbc.BankAccount;
import me.staek.jdbc.repository.BankAccountRepositoryV3;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.SQLException;

/**
 * 트랜잭션 - 트랜잭션 매니저(PlatformTransactionManager) 템플릿콜백 패턴으로 구현한 TransactionTemplate를 이용하도록 로직변경
 */
@Slf4j
public class BankAccountServiceV3_2 {

    private final TransactionTemplate txTemplate;
    private final BankAccountRepositoryV3 accountRepository;

    public BankAccountServiceV3_2(PlatformTransactionManager transactionManager, BankAccountRepositoryV3 accountRepository) {
        this.txTemplate = new TransactionTemplate(transactionManager);
        this.accountRepository = accountRepository;
    }

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        txTemplate.executeWithoutResult((status) -> {
            try {
                bizLogic(fromId, toId, money);
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        });
    }

    private void bizLogic(String fromId, String toId, int money) throws SQLException {
        BankAccount fromMember = accountRepository.findById(fromId);
        BankAccount toMember = accountRepository.findById(toId);

        accountRepository.update(fromId, fromMember.getMoney() - money);
        validation(toMember);
        accountRepository.update(toId, toMember.getMoney() + money);
    }

    private void validation(BankAccount toMember) {
        if (toMember.getAccountId().equals("ex")) {
            throw new IllegalStateException("이체중 예외 발생");
        }
    }

}
