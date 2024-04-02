package hello.core.step2.member;

public interface MemberRepository {

    void save(Member member);
    Member findById(Long id);
}
