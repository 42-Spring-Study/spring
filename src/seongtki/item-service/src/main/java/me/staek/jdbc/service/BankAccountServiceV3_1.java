package me.staek.jdbc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.staek.jdbc.BankAccount;
import me.staek.jdbc.repository.BankAccountRepositoryV3;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.SQLException;

/**
 * 트랜잭션 - 트랜잭션 매니저를 사용해서 계좌이체 서비스로직 작성
 */
@Slf4j
@RequiredArgsConstructor
public class BankAccountServiceV3_1 {

    private final PlatformTransactionManager transactionManager;
    private final BankAccountRepositoryV3 accountRepository;

    public void accountTransfer(String fromId, String toId, int money) {
        //트랜잭션 시작
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            //비즈니스 로직
            bizLogic(fromId, toId, money);
            transactionManager.commit(status); //성공시 커밋
        } catch (Exception e) {
            transactionManager.rollback(status); //실패시 롤백
            throw new IllegalStateException(e);
        }

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
