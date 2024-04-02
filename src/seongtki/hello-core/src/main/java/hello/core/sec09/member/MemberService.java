package hello.core.sec09.member;

public interface MemberService {
    void join(Member member);
    Member findMember(Long id);
}
