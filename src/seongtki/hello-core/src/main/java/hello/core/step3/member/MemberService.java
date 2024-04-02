package hello.core.step3.member;

public interface MemberService {
    void join(Member member);
    Member findMember(Long id);
}
