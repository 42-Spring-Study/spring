package hello.core.step1.member;

import java.util.HashMap;
import java.util.Map;

public class DBMemberRepository implements MemberRepository {

    public static Map<Long, Member> store = new HashMap<>();
    public static Long seq = 0L;
    @Override
    public void save(Member member) {
        store.put(++seq, member);
    }

    @Override
    public Member findById(Long id) {
        return store.get(id);
    }
}
