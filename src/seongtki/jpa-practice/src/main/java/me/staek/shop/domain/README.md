### 일대다, 다대일 테이블에 대해 JPA Entity 설계 스토리
~~~

<외래키를 변수로 설정>
Join을 위한 연관관계를 설정할 때, 외래키 변수를 작성하면
객체설계가 안되어 코드레벨에서 로직을 작성해야 하므로 복잡하다.

Team team = new Team();
team.setName("team name");

Member member = new Member();
member.setName("member name");
em.persist(team);
member.setTeamId(team.getId());
em.persist(member);


<단방향 설정>
일반적으로 db table의 외래키와 같은 위치에 jpa 참조관계를 설정하는것이 좋다.  
(1:M 상황에서 M 쪽을 말한다.)

@ManyToOne @JoinColumn("TEAM_ID") Team team;
=> 위와같이 작성하고 실제 데이터 세팅 시 member.setTeam(team); 메모리에 참조객체를 삽입하면
=> flush 할 때 왜래키 값이 삽입된다.
=> 이 후 db에서 데이터 조회 시 team에 해당 정보를 객체 그래프를 통해 조회할 수 있다. (지연로딩 시 사용시점에 입력됨)

<양방향 설정>
반대 쪽 Entity에서 역으로 조회할 상황에서 필요하다.
M : @ManyToOne @JoinColumn("TEAM_ID") Team team;
1 : @OneToMany(mappedby = team) List<Member> members;

1쪽 참조객체를 살펴보면 mappedby 키워드가 있는데, M쪽의 참조객체를 참조한다는 의미이다.
db 조회 시 해당 참조에 대한 값을 1차캐시에 담아 조회할 수 있다. (쓰기는 할 수 없다. 쓰기는 오로지 연관관계 주인인 M쪽에서만 가능하다) 

위 처럼 작성하면 flush() 이후 db로부터 값을 불러올 때
1차 캐시에 참조에 대한 값이 모두 입력된다. (지연로딩일 경우 사용시점에 입력됨)
하지만 flush() 이전인 상황에 메모리에 있는 참조를 사용하려한다면 당연히 값이 없을 것이다.
이럴 경우에는 addMember() 와 같은 메서드를 이용해 양쪽 객체 모드에 참조값을 입력하도록 로직을 작성하자.


~~~


~~~

<단방향 설정>
@OneToMany @JoinColumn(name = "TEAM_ID")
private List<Member> members = new ArrayList<>();

1쪽 객체인 Team에 참조객체를 설정한다. Member 테이블의 외래키(TEAM_ID) 수정권한 존재.
    

<양방향 설정>
@ManyToOne @JoinColumn(name = "TEAM_ID", insertable = false, updatable = false)
private Team team;

M쪽 객체인 Member에 참조객체를 설정한다. @JoinColumn 설정에 대해 컬럼 수정권한을 제한하기 위한 옵션을 추가한다.

~~~

### <일대다 문제점>
~~~

참조객체 수정권한을 1쪽에서 갖고 있다.

다대일 관계에서 로직전개를 보면
Team 이 먼저 등록되고나서 Member가 등록된다. Member가 등록될 때 TEAM_ID가 같이 등록된다.
Member가 참조객체 수정권한이 있으므로 쿼리는 총 두개로 마무리 된다.

반면 일대다 관계에서는
Team 이 먼저 등록되고나서 Member가 등록된다. Member가 등록될 때 TEAM_ID은 삽입될 수 없다.
Member는 참조객체를 수정할 권한이 없기 때문이다.
이후 Team 참조객체에 의해 Member 객체에 TEAM_ID가 갱신되는 쿼리가 실행되어 
총 3개의 쿼리가 실행된다.

쿼리에대한 성능 뿐 아니라

다대일에서 MEMBER 등록 시 TEAM_ID 가 같이 같은 테이블에 등록되지만
일대다에서는 MEMBER 등록 이후 TEAM 참조객체에 의해 다시한번 업데이트 쿼리가 실행되어
기능하나에 관점이 여려군데로 옮겨지는 이슈가 존재한다.

~~~
