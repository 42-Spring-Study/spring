package me.staek.jdbc.repository;

import lombok.extern.slf4j.Slf4j;
import me.staek.jdbc.BankAccount;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;

/**
 * JdbcTemplate 사용
 */
@Slf4j
public class BankAccountRepositoryV5 implements AccountRepository {

    private final JdbcTemplate template;

    public BankAccountRepositoryV5(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public BankAccount save(BankAccount account) {
        String sql = "insert into account(account_id, money) values (?, ?)";
        template.update(sql, account.getAccountId(), account.getMoney());
        return account;
    }

    @Override
    public BankAccount findById(String accountId) {
        String sql = "select * from account where account_id = ?";
        return template.queryForObject(sql, memberRowMapper(), accountId);
    }

    @Override
    public void update(String accountId, int money) {
        String sql = "update account set money=? where account_id=?";
        template.update(sql, money, accountId);
    }

    @Override
    public void delete(String accountId) {
        String sql = "delete from account where account_id=?";
        template.update(sql, accountId);
    }

    private RowMapper<BankAccount> memberRowMapper() {
        return (rs, rowNum) -> {
            BankAccount member = new BankAccount();
            member.setAccountId(rs.getString("account_id"));
            member.setMoney(rs.getInt("money"));
            return member;
        };
    }

}
