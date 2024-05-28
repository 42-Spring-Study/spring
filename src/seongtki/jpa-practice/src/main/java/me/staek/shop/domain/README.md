~~~
일대다, 다대일 테이블에 대해 JPA Entity 설계 스토리

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
(1:N 상황에서 N 쪽을 말한다.)

@ManyToOne @JoinColumn("TEAM_ID") Team team;
=> 위와같이 작성하고 실제 데이터 세팅 시 member.setTeam(team); 메모리에 참조객체를 삽입하면
=> flush 할 때 왜래키 값이 삽입된다.
=> 이 후 db에서 데이터 조회 시 team에 해당 정보를 객체 그래프를 통해 조회할 수 있다. (지연로딩 시 사용시점에 입력됨)

<양방향 설정>
반대 쪽 Entity에서 역으로 조회할 상황에서 필요하다.
N : @ManyToOne @JoinColumn("TEAM_ID") Team team;
1 : @OneToMany(mappedby = team) List<Member> members;

1쪽 참조객체를 살펴보면 mappedby 키워드가 있는데, M쪽의 참조객체를 참조한다는 의미이다.
db 조회 시 해당 참조에 대한 값을 1차캐시에 담아 조회할 수 있다. (쓰기는 할 수 없다. 쓰기는 오로지 연관관계 주인인 M쪽에서만 가능하다) 

위 처럼 작성하면 flush() 이후 db로부터 값을 불러올 때
1차 캐시에 참조에 대한 값이 모두 입력된다. (지연로딩일 경우 사용시점에 입력됨)
하지만 flush() 이전인 상황에 메모리에 있는 참조를 사용하려한다면 당연히 값이 없을 것이다.
이럴 경우에는 addMember() 와 같은 메서드를 이용해 양쪽 객체 모드에 참조값을 입력하도록 로직을 작성하자.


~~~
