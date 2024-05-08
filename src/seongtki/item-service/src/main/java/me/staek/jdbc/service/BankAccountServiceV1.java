package me.staek.jdbc.service;

import lombok.RequiredArgsConstructor;
import me.staek.jdbc.BankAccount;
import me.staek.jdbc.repository.BankAccountRepositoryV1;

import java.sql.SQLException;

/**
 * 계좌이체 기능 구현
 * - 특정 account_id 에대해 업데이트하지 않도록 검증로직을 삽입한다.
 */
@RequiredArgsConstructor
public class BankAccountServiceV1 {

    private final BankAccountRepositoryV1 accountRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {

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
