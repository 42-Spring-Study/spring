package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MemoryMemberRepositoryTest {

    MemoryMemberRepository repository = new MemoryMemberRepository();

    @AfterEach
    void afterEach() {
        repository.clearStore();
    }

    @Test
    void save() {
        // given
        Member member = new Member( "seongtki");

        // when
        Member saved = repository.save(member);

        // then
        Member result = repository.findById(saved.getId()).get();
        Assertions.assertThat(result).isEqualTo(member);
    }

    @Test
    void findById() {
        // given
        Member m1 = new Member( "seongtki");

        // when
        repository.save(m1);

        // then
        Optional<Member> finded = repository.findById(m1.getId());
        Assertions.assertThat(finded.get().getId()).isEqualTo(m1.getId());
        Assertions.assertThat(finded.get()).isEqualTo(m1); // equals 비교
    }

    @Test
    void findByName() {
        // given
        Member m1 = new Member( "seongtki");

        // when
        repository.save(m1);

        // then
        Optional<Member> finded = repository.findByName(m1.getName());
        Assertions.assertThat(finded.get().getName()).isEqualTo(m1.getName());
    }

    @Test
    void findAll() {
        // given
        Member m1 = new Member( "seongtki");
        Member m2 = new Member( "seongtaekkim");

        // when
        repository.save(m1);
        repository.save(m2);

        // then
        Assertions.assertThat(2).isEqualTo(repository.findAll().size());
    }
}
