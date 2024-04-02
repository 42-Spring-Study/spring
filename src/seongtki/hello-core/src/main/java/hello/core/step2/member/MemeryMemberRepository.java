package hello.core.step2.member;

import java.util.HashMap;
import java.util.Map;

public class MemeryMemberRepository implements MemberRepository {

    public static Map<Long, Member> store = new HashMap<>();
    @Override
    public void save(Member member) {
        store.put(member.getId(), member);
    }

    @Override
    public Member findById(Long id) {
        return store.get(id);
    }

    public void clear() {
        store.clear();
    }
}
