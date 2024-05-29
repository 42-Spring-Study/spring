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


### 1대1

~~~
주테이블 Member(1) : 타겟테이블 Locker(1) 에서 연관관계 설정

<단방향>
Member에 참조관계를 설정한다

@OneToOne
@JoinColumn(name = "LOCKER_ID")
private Locker locker;

=> 참조관계로 해당 컬럼 수정권한을 갖는다.
    
    
<양방향>
@OneToOne(mappedBy = "locker")
private Member member;

=> mappedBy를 설정한다


<외래키를 어느 테이블에 설정하나>

<타겟 테이블에 설정할 경우 문제>
주 테이블이 아닌 타겟테이블에 외래키를 설정할 경우, 연관관계 매핑은 타겟 객체가 되어야한다.
비지니스 컨텍스트가 주 테이블에 의해 관리된다면, 타겟테이블이 연관관계 매핑할 시 관리 포인트가 늘어나게 되고 또한 지연로딩 설정이 불가능하다.
개발자 입장에서는 현재 방법이 효율적이다.

<주 테이블에 설정할 경우 문제>
한편, 주 테이블에 외래키를 설정하고 주 객체와 연관관계를 매핑할 경우 타겟테이블과 관계가 1대다 가 될 수 있다면
변경에 대한 트레이드오프를 생각해보아야 한다.


~~~


### N:M

~~~
N대M 테이블이라는건 존재할 수 없다.
양쪽 테이블 모두 상대 테이블 key를 추가해야 조건을 만족하기 때문에, 결국 같은 key의 다른 일반컬럼을가진 두개 테이블이 된다.

이를 해결하기 위해 N:M 매핑 태이블을 만든다.
매핑 테이블은 양쪽 테이블과 1:M 관계를 유지하고, 양쪽 테이블 key에 대해 왜래키이자 기본키인 식별관계를 설정하게 된다.

Member (N) : Product (M) 관계에서 아래처럼 참조객체 매핑을 할 수 있는데(양방향), 
PRODUCT_MEMBER 이름의 매핑테이블이 식별관계로 생성된다.

<Product, 참조객체 매핑>
@ManyToMany
@JoinTable(name = "PRODUCT_MEMBER")
private List<Member> members = new ArrayList<>();

<Member, Product에 일기전용 매핑>
@ManyToMany(mappedBy = "members")
private List<Product> products = new ArrayList<>();


다만, 현업에서는 매핑테이블에 추가 컬럼사용을 많이 하기 때문에 해당기능은 사용하지 않는다.
추가적으로 매핑테이블의 식별관계 역시 비지니스 로직에 유연하지 못한 경우가 많아 비식별관계로 작성해 사용한다. 

매핑테이블을 따로 생성할 시 아래와 같이 양방향 설정할 수 있다.

<PRODUCT_MEMBER - 단방향 참조객체 매핑>
@ManyToOne
@JoinColumn(name = "PRODUCT_ID")
private Product product;

@ManyToOne
@JoinColumn(name = "MEMBER_ID")
private Member member;

<MEMBER - 양방향 참조객체>
@OneToMany(mappedBy = "member")
private List<ProductMember> productMembers = new ArrayList<>();

<PRODUCT - 양방향 참조객체>
@OneToMany(mappedBy = "product")
private List<ProductMember> productMembers = new ArrayList<>();

~~~


### 상속관계
~~~

@Inheritance(strategy = InheritanceType.SINGLE_TABLE) 
- 기본전략이다.
- @DiscriminatorColumn로 구분자를 통해 하위 계층을 구분하여 사용할 수 있다.
- 테이블이 하나이기 때문에 성능이 기본적으로 좋지만 관리를 잘 해야함.

@Inheritance(strategy = InheritanceType.JOINED)
- DB의 상속관계를 만들어 생성한다.
- 구조화가 잘 되어 있지만 기본적으로 복잡하다.
- 주요하고 데이터가 매우 많은 테이블이라면 트레이드오프 해서 사용하자.

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
- 부모 키를 가지고 각각 테이블이 생성된다. 서로 관계가 없어서 데이터를 상위객체 (Item)으로 조회 시 union 조회하여 성능에 좋지 않다.
- 쓸 이유가 딱히 없다/

@DiscriminatorColumn
- 기본 객체명 : DTYPE 으로 입력된다
- 하위계층 구분자라고 보면된다.
~~~
