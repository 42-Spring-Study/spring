package hello.core.sec07.member;

public interface MemberService {
    void join(Member member);
    Member findMember(Long id);
}
