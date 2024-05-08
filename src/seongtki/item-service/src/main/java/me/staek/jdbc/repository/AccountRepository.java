package me.staek.jdbc.repository;


import me.staek.jdbc.BankAccount;

public interface AccountRepository {
    BankAccount save(BankAccount account);

    BankAccount findById(String accountId);

    void update(String accountId, int money);

    void delete(String accountId);

}
