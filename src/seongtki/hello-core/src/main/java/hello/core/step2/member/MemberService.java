package hello.core.step2.member;

public interface MemberService {
    void join(Member member);
    Member findMember(Long id);
}
