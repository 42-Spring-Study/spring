package me.staek.jdbc.repository;

import lombok.extern.slf4j.Slf4j;
import me.staek.jdbc.BankAccount;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;


@Slf4j
class BankAccountRepositoryV0Test {


    BankAccountRepositoryV0 repository = new BankAccountRepositoryV0();

    @Test
    void crud() throws SQLException {
        //save
        BankAccount member = new BankAccount("memberV100", 10000);
        repository.save(member);

        //findById
        BankAccount findMember = repository.findById(member.getAccountId());
        log.info("findMember={}", findMember);
        assertThat(findMember).isEqualTo(member);

        //update: money: 10000 -> 20000
        repository.update(member.getAccountId(), 20000);
        BankAccount updatedMember = repository.findById(member.getAccountId());
        assertThat(updatedMember.getMoney()).isEqualTo(20000);

        //delete
        repository.delete(member.getAccountId());
        assertThatThrownBy(() -> repository.findById(member.getAccountId()))
                .isInstanceOf(NoSuchElementException.class);
    }
}
