package hello.core.sec09.member;

public interface MemberRepository {

    void save(Member member);
    Member findById(Long id);
}
