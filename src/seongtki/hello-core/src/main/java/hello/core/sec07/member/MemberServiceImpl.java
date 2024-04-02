package hello.core.sec07.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component("order")
@Component
public class MemberServiceImpl implements MemberService {

    private final MemberRepository repository;

    @Autowired
    public MemberServiceImpl(MemberRepository repository) {
        this.repository = repository;
    }


    @Override
    public void join(Member member) {
        repository.save(member);
    }

    @Override
    public Member findMember(Long id) {
        return repository.findById(id);
    }


    //테스트 용도
    public MemberRepository getMemberRepository() {
        return repository;
    }
}
