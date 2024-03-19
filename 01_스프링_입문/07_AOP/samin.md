##### AOP
언제 필요한가?
함수가 실행될떄마다 실행 시간을 알고싶을떄 
어떻게 구현할까? - 모든 함수에 시간 측정 기능 구현 하거나 호출
그런데 그런 동작이 1000개 이상이라면, 그때 수정이 필요하다면 ...

핵심 관심사와 공통관심사가 섞여 수정이 어려울때 유용하게 사용이 가능하다.

공통관심사,
핵심관심사


##### AOP 적용
@Aspect  
~~~
@Around("execution(* hello.hellospring..*(..))")
public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {

//공통관심사 코드 장성
}

~~~