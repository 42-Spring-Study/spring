package hello.core.sec06.member;

public interface MemberRepository {

    void save(Member member);
    Member findById(Long id);
}
