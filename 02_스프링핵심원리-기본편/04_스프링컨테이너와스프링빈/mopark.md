# 섹션 4- 스프링 컨테이너와 스프링 빈

`ApplicationContext` 를 스프링 컨테이너라 하고 이건 인터페이스라서 실체 구현된 클래스는 여러개이다.

그 중 사용한게 `AnnotationConfigApplicationContext` 이다

getBean으로 조회 가능하고, 이름, 타입으로 조회 가능하다. 없는 이름의 @Bean을 조회하면 예외 발생

또한 타입으로 조회할 때 같은 타입이 2이상이면 예외 발생

같은 타입을 한번에 조회하고 싶으면 getBeansOfType으로 조회

상속관계를 이용해 부모타입을 이용해 조회시 마찬가지로 getBean을 이용하면 2개 이상이면 예외 발생 

조회 하고싶으면 이름과 함께 타임으로 조회하거나 getBeansOfType으로 조회

Object 타입은 모두의 부모이기 때문에 모두 조회한다.

BeanFactory가 스프링 컨테이너의 최상위 인터페이스

빈을 관리하고 조회하는 기능

ApplicationContext는 BeanFactory를 상속받음

그럼 차이가 뭐야? → 추가적인 부가기능

XML로도 `@Configuration` 설정 가능

다양한 설정 형식을 지원할 수 있는 이유? → BeanDefinition으로 추상화

메타데이터란?
.