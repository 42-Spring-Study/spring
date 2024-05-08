package me.staek.jdbc;

import lombok.Data;
@Data
public class BankAccount {
    private String accountId;
    private int money;
    public BankAccount() {
    }
    public BankAccount(String accountId, int money) {
        this.accountId = accountId;
        this.money = money;
    }
}
