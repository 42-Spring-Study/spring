package hello.core.step3.member;

public interface MemberRepository {

    void save(Member member);
    Member findById(Long id);
}
