##### h2 데이터베이스 설치
https://www.h2database.com]()
다운로드 및 설치  
h2 데이터베이스 버전은 스프링 부트 버전에 맞춘다. 권한 주기: `chmod 755 h2.sh` **(윈도우 사용자는 x)** 실행: `./h2.sh` **(윈도우 사용자는 h2.bat)** 데이터베이스 파일 생성 방법

`jdbc:h2:~/test` (최초 한번)

`~/test.mv.db` 파일 생성 확인  
이후부터는 `jdbc:h2:tcp://localhost/~/test` 이렇게 접속

##### 순수 JDBC
**build.gradle 파일에 jdbc, h2 데이터베이스 관련 라이브러리 추가** 
```
implementation 'org.springframework.boot:spring-boot-starter-jdbc' runtimeOnly 'com.h2database:h2'
```

**스프링 부트 데이터베이스 연결 설정 추가**
resources/application.properties
```

spring.datasource.url=jdbc:h2:tcp://localhost/~/test

spring.datasource.driver-class-name=org.h2.Driver

spring.datasource.username=sa
```

인터페이스 기반으로 구현
JDBC 데이터 소스 주입
~~~

private final DataSource dataSource;

public JdbcMemberRepository(DataSource dataSource) { this.dataSource = dataSource;

}
~~~

DataSource dataSource

Connection 
PreparedStatement
ResultSet
DataSourceUtils.getConnection(dataSource);

DataSourceUtils.releaseConnection(conn, dataSource);
##### 스프링 통합 테스트
@SpringBootTest : 스프링 컨테이너와 테스트를 함께 실행한다.
테스트케이스에 @Tranjectional 달면 테스트 종료후 롤백해준다. db테스트에 유용함

##### 스프링 JDBCTemplate
jdbc템플릿 주입
~~~
private final JdbcTemplate jdbcTemplate;

public JdbcTemplateMemberRepository(DataSource dataSource) { jdbcTemplate = new JdbcTemplate(dataSource);

}

~~~

jdbcTemplate.query("sql", memberRowMapper)

~~~
private RowMapper<Member> memberRowMapper() { return (rs, rowNum) -> {

Member member = new Member(); member.setId(rs.getLong("id")); member.setName(rs.getString("name")); return member;

}; }

~~~

##### JPA
반복되는 코드와 SQL을 작성해준다!


build.gradle 파일에 JPA, h2 데이터베이스 관련 라이브러리 추가

~~~
dependencies {  
implementation 'org.springframework.boot:spring-boot-starter-thymeleaf' implementation 'org.springframework.boot:spring-boot-starter-web'

// implementation 'org.springframework.boot:spring-boot-starter-jdbc' implementation 'org.springframework.boot:spring-boot-starter-data-jpa' runtimeOnly 'com.h2database:h2' testImplementation('org.springframework.boot:spring-boot-starter-test') {

exclude group: 'org.junit.vintage', module: 'junit-vintage-engine' }

}
~~~


**스프링 부트에 JPA 설정 추가** `resources/application.properties`

~~~
spring.datasource.url=jdbc:h2:tcp://localhost/~/test spring.datasource.driver-class-name=org.h2.Driver spring.datasource.username=sa

spring.jpa.show-sql=true spring.jpa.hibernate.ddl-auto=none

~~~

`show-sql` : JPA가 생성하는 SQL을 출력한다.  
`ddl-auto` : JPA는 테이블을 자동으로 생성하는 기능을 제공하는데 `none` 를 사용하면 해당 기능을 끈다. `create` 를 사용하면 엔티티 정보를 바탕으로 테이블도 직접 생성해준다.

@Entity    JPA가 관리하는 엔티티라는 뜻 

@Id   @GeneratedValue(strategy = GenerationType.IDENTITY) PK라는 뜻


엔티티 메니저 주입

~~~
 private final EntityManager em;
 

public JpaMemberRepository(EntityManager em) { 
	this.em = em;
}
~~~

##### 스프링 데이터 JPA
스프링 데이터 JPA가 어떤 도움을 주는지?
공통으로 사용하는것들은 미리 만들어줌 
~~~
1. `findById(ID id)`: 주어진 ID에 해당하는 엔티티를 반환합니다.
2. `findAll()`: 모든 엔티티를 반환합니다.
3. `save(S entity)`: 엔티티를 저장합니다.
4. `deleteById(ID id)`: 주어진 ID에 해당하는 엔티티를 삭제합니다.
5. `count()`: 엔티티의 총 수를 반환합니다.
6. `existsById(ID id)`: 주어진 ID에 해당하는 엔티티가 존재하는지 여부를 확인합니다.
7. `deleteAll()`: 모든 엔티티를 삭제합니다.
~~~

그러나 비즈니스의 복잡성때문에 모든것을 공통화 할수는 없음 
그럴 때는 이름조합으로 가능 
~~~
`Optional<Member> findByNameAndId(String name, Long id);`
~~~

챗gpt의 답변
스프링 데이터 JPA는 다양한 검색 조건에 따라 복수의 값을 반환할 수 있는 메서드도 제공합니다. 여러 값으로 조회할 때에는 다음과 같은 방법을 사용할 수 있습니다:

1. **Query Methods의 사용**: Query Methods를 사용하여 여러 값으로 조회할 수 있습니다. 예를 들어, 이름(name)과 ID(id)로 멤버(Member)를 조회하는 메서드를 만들고 싶다면 다음과 같이 메서드를 추가할 수 있습니다.

javaCopy code

`Optional<Member> findByNameAndId(String name, Long id);`

위의 메서드는 이름(name)과 ID(id)를 조건으로 사용하여 멤버(Member)를 조회합니다. 이름과 ID는 AND 연산자로 결합되어야 합니다.

2. **@Query 어노테이션 사용**: @Query 어노테이션을 사용하여 직접 JPQL(Query Methods와 비슷한 형식의 SQL-like 쿼리)을 정의할 수도 있습니다. 이를 통해 보다 복잡한 쿼리를 작성할 수 있습니다.

javaCopy code

`@Query("SELECT m FROM Member m WHERE m.name = :name AND m.id = :id") Optional<Member> findByNameAndId(@Param("name") String name, @Param("id") Long id);`

위의 코드에서 ":name"과 ":id"는 JPQL의 named parameter입니다. @Param 어노테이션을 사용하여 메서드 파라미터와 매핑됩니다.

이렇게 하면 이름(name)과 ID(id)를 조건으로 사용하여 멤버(Member)를 조회할 수 있습니다. 원하는 필드를 사용하여 조건을 지정하고, 필요에 따라 쿼리 메서드를 추가로 작성할 수 있습니다.






각 방법들 스프링 설정관련 내용 추가 필요.
