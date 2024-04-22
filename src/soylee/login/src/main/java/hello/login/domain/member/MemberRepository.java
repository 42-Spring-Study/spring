package hello.login.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
public class MemberRepository {
    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    public Member save(Member member){
        member.setId(++sequence);
        store.put(member.getId(), member);
        log.info("save: member={}", member.getId());
        return member;
    }

    public Member findById(long id) {
        return store.get(id);
    }

    public Optional<Member> findByLoginId(String loginId) {
        return getStoreList().stream()
                .filter(m -> m.getLoginId().equals(loginId))
                .findFirst();
    }

    public void clearStore(){
        store.clear();
    }

    private static ArrayList<Member> getStoreList() {
        return new ArrayList<>(store.values());
    }

}
