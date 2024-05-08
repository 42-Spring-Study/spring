package me.staek.jdbc.service;

import lombok.extern.slf4j.Slf4j;
import me.staek.jdbc.BankAccount;
import me.staek.jdbc.repository.AccountRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 예외 누수 문제 해결
 * SQLException 제거
 *
 * accountRepository 인터페이스 의존
 */
@Slf4j
public class BankAccountServiceV4 {

    private final AccountRepository accountRepository;

    public BankAccountServiceV4(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public void accountTransfer(String fromId, String toId, int money) {
        bizLogic(fromId, toId, money);
    }

    private void bizLogic(String fromId, String toId, int money) {
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
