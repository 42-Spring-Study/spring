package me.staek.jdbc.service;

import lombok.extern.slf4j.Slf4j;
import me.staek.jdbc.BankAccount;
import me.staek.jdbc.repository.BankAccountRepositoryV3;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

/**
 * 선언적 트랜잭션 적용
 * - @Transactional AOP
 */
@Slf4j
public class BankAccountServiceV3_3 {

    private final BankAccountRepositoryV3 accountRepository;

    public BankAccountServiceV3_3(BankAccountRepositoryV3 accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        bizLogic(fromId, toId, money);
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
