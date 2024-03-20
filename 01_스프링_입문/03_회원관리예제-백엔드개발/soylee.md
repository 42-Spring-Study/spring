# 비즈니스 요구사항 정리

## 일반적인 웹 어플리케이션 계층 구조

![스크린샷 2024-03-19 오후 7.05.49.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2e635c72-05ea-4289-aeec-6a010538da4f/1a60eb38-2596-4eac-bb84-6f093d04465c/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA_2024-03-19_%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE_7.05.49.png)

- 컨트롤러: 웹 MVC의 컨트롤러, 클라이언트 이요하는 앤드포인트
- 서비스: 핵심 비즈니스 로직 구현, http 통신 객ㅌ체에 직접 접근 불가
- 리포지토리: 데이터베이스 접근, 도메인 객체를 DB에 저장하고 관리DAO
- 도메인: 비즈니스 도메인 객체( 즉, DB 테이블과 매핑되는 객체)DTO

## 비즈니스 요구사항

- 데이터: 회원 ID, 이름
- 기능: 회원 등록, 회원 조회
- 아직 데이터 저장소가 선정되지 않음

### 클래스 의존관계

![스크린샷 2024-03-19 오후 7.13.37.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/2e635c72-05ea-4289-aeec-6a010538da4f/bad32cef-fac5-4af4-a4e7-3a1f07d19d56/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA_2024-03-19_%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE_7.13.37.png)

- 데이터저장소는 RDB, NoSQL 등 다양한 저장소 고민 중이다.
- 레파지토리를 인터페이스로 설계하여, 추후 데이터 저장소 선정 시 쉽게 교체할 수 있도록 함
- 초기 개발 단계이므로, 가벼운 메모리 기반의 데이터 저장소를 사용한 구현체 사용

# 회원 도메인과 리포지토리 구현하기

## 회원 도메인

```java
package hello.hellospring.domain;

public class Member {
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

## 리포지토리

### 인터페이스

```java
package hello.hellospring.repository;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(Long id);
    Optional<Member> findByName(String name);
    List<Member> findAll();
}
```

### 메모리 구현체

아래의 코드는 동시성 문제가 고려되지 않았다. 실무에서는 ConcurrentHashMap, AtomicLong 사용을 고려해야 한다.

```java
package hello.hellospring.repository;

public class MemoryMemberRepository implements MemberRepository{

    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    @Override
    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Member> findByName(String name) {
        return store.values().stream()
                .filter(member -> member.getName.equals(name))
                .findAny();
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }
}
```

- Optional:??
- 람다식:??

# 회원 리포지토리 테스트 케이스 작성

기존 테스트 방법: 자바의 main 메서드에서 실행하거나, 웹 어플리케이션의 컨트롤러를 통해 해당 기능을 실행

→ 실행 시 오래걸림, 반복 실행 어려움, 여러 테스트 한 번에 실행하기 어렵다는 단점

→ JUnit이라는 테스트 프레임워크를 실행해 문제 해결

```java
package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MemoryMemberRepositoryTest {
    MemoryMemberRepository repository = new MemoryMemberRepository();

    @AfterEach
    public void afterEach(){
        repository.clearStore();
    }

    @Test
    public void save(){
        //given
        Member member = new Member();
        member.setName("spring");

        //when
        repository.save(member);

        //then
        Member result = repository.findById(member.getId()).get();
        assertThat(result).isEqualTo(member);
    }

    @Test
    public void findByName(){
        //given
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        //when
        Member result = repository.findByName(member1.getName()).get();

        //then
        assertThat(result).isEqualTo(member1);
//        assertThat(result.getName()).isEqualTo(member2.getName());
    }

    @Test
    public void findAll() {
        //given
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        //when
        List <Member> result = repository.findAll();

        //then
        assertThat(result.size()).isEqualTo(2);
    }
}

```

테스트클래스 네이밍 관례: test할 클래스명 + Test

@Test

@AfterEach

각 테스트는 독립적으로 실행되어야 한다. 테스트 순서에 의존관계가 있는 건 좋은 테스트가 아니다.

# 회원 서비스 구현하기

```java
package hello.hellospring.service;

public class MemberService {
    private final MemberRepository memberRepository = new MemoryMemberRepository();

    public Long join(Member member){
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(Long memberId){
        return memberRepository.findById(memberId);
    }
}
```

# 회원 서비스 테스트

기존 service에서는 Repo를 직접 생성했었는데, DI가 가능하도록 변경

```java
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }
    ...
}
```

테스트 코드는 다음과 같다

```java
public class MemberServiceTest {
    MemberService memberService;
    MemoryMemberRepository memberRepository;

		//각 테스트 실행 전 호출된다
    @BeforeEach
    public void beforeEach(){
		    //테스트 간 서로 영향 없도록 새로운 객체 생성 및 의존관계 맺음
        memberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memberRepository);
    }

    @AfterEach
    public void afterEach(){
        memberRepository.clearStore();
    }

    @Test
    public void 회원가입() throws Exception {
        //Given
        Member member = new Member();
        member.setName("hello");

        //when
        Long saveId = memberService.join(member);

        //Then
        Member findMember = memberService.findOne(saveId).get();
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    // 항상 잘못된 데이터가 입력되었을 때, 잘 터지는지도 확인해야 한다!
    public void 중복_이름_회원_예외() throws Exception {
        //Given
        Member member1 = new Member();
        member1.setName("spring");

        Member member2 = new Member();
        member2.setName("spring");

        //When
        memberService.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class, ()->memberService.join(member2));

        //Then
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    }
}
```
