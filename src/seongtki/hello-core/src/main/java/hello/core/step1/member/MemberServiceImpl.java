package hello.core.step1.member;

public class MemberServiceImpl implements MemberService {

    private final MemberRepository repository = new MemeryMemberRepository(); // DIP 위반
    @Override
    public void join(Member member) {
        repository.save(member);
    }

    @Override
    public Member findMember(Long id) {
        return repository.findById(id);
    }
}
