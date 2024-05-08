package me.staek.jdbc.repository;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import me.staek.jdbc.BankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static me.staek.jdbc.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class BankAccountRepositoryV1Test {

    BankAccountRepositoryV1 repository;

    @BeforeEach
    void beforeEach() {
        // 기본 DriverManager - 항상 새로운 커넥션을 획득
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

        // 커넥션 풀링
//        HikariDataSource dataSource = new HikariDataSource();
//        dataSource.setJdbcUrl(URL);
//        dataSource.setMaximumPoolSize(10);
//        dataSource.setUsername(USERNAME);
//        dataSource.setPoolName(PASSWORD);
        repository = new BankAccountRepositoryV1(dataSource);
    }

    @Test
    void crud() throws SQLException {
        //save
        BankAccount account = new BankAccount("memberV100", 10000);
        repository.save(account);

        //findById
        BankAccount findMember = repository.findById(account.getAccountId());
        log.info("findMember={}", findMember);
        assertThat(findMember).isEqualTo(account);

        //update: money: 10000 -> 20000
        repository.update(account.getAccountId(), 20000);
        BankAccount updatedMember = repository.findById(account.getAccountId());
        assertThat(updatedMember.getMoney()).isEqualTo(20000);

        //delete
        repository.delete(account.getAccountId());
        assertThatThrownBy(() -> repository.findById(account.getAccountId()))
                .isInstanceOf(NoSuchElementException.class);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
