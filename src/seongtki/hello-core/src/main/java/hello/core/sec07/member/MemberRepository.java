package hello.core.sec07.member;

public interface MemberRepository {

    void save(Member member);
    Member findById(Long id);
}
