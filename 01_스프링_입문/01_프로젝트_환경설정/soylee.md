# 1. 프로젝트 환경 설정

# 프로젝트 생성

[스프링 부트 스타터 사이트](https://start.spring.io)에서 프로젝트 생성

- `Gradle - Groovy`, `Java`, `Jar` 선택
- Dependencies: `Spring Web`, `Thymeleaf` 선택

## 프로젝트 디렉터리 구조

- `.gradle`
- `.idea`: intelliJ 설정파일
- `gradle/`:
- `src`
    - `main`
        - `java/`: java 파일
        - `resources/`: xml, html, property 등
            - static: 정적파일
            - templates: 동적파일
    - `test`: Test 관련 소스
- `build.gradle` : gradle을 통해 빌드될 수 있도록 설정하는 파일
- `setting.gradle`: 앱 빌드 시 포함 모듈 알려주는 파일

## Gradle 설정 - `build.gradle`

```groovy
plugins {
	id 'java'
	id 'org.springframework.boot' version '3.2.3'
	id 'io.spring.dependency-management' version '1.1.4'
}

group = 'hello'
version = '0.0.1-SNAPSHOT'

// 자바 버전: 17
java {
	sourceCompatibility = '17' 
}

repositories {
	mavenCentral() //dependency 라이브러리 다운로드 사이트
}

//spring boot starter에서 선택한 Dependency 포함
dependencies { 
	implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	//기본적으로 테스트라이브러리 추가(Junit5)
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
}

tasks.named('test') {
	useJUnitPlatform()
}
```

# 라이브러리 살펴보기

빌드 툴인 Gradle은 의존관계가 있는 라이브러리를 함께 다운로드 한다.

### 스프링 부트 라이브러리

- spring-boot-starter-web
    - spring-boot-starter-tomcat: 톰캣 서버, spring 에 내장되어 있다.
    - spring-webmvc: 스프링 웹 MVC
- spring-boot-starter-thymleaf: 타임리프 템플릿 엔진(MVC 중 V)
- spring-boot-starter: 위 두 라이브러리가 공통으로 의존하는 라이브러리, 스프링 부트, 스프링 코어, 로깅 라이브러리 등을 포함한다
    - spring-boot
        - spring-core
    - spring-boot-starter-logging
        - logback(구현체), slf4j(인터페이스)

### 테스트 라이브러리

- spring-boot-stater-test
    - junit: 테스트 프레임워크
    - spring-test: 스프링 통합 테스트 지원

# View 환경설정

### Welcome Page

`static/index.html` 가 도메인네임으로 접근했을 때 얻을 수 있는 Welcome Page이다. 

### thymeleaf 템플릿 엔진

1. `java/hello/hellospring/controller`에 컨트롤러 생성한다
    
    ```java
    package hello.hellospring.controller;
    
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.GetMapping;
    
    @Controller
    public class HelloController {
        @GetMapping("hello")
        public String hello(Model model){
            model.addAttribute("data", "spring!!");
            return "hello";
        }
    }
    ```
    
2. `resources/templates`에 html 파일 생성한다.
    
    ```html
    <!DOCTYPE html>
    <html lang="en" xmlns:th="http://www.thymeleaf.org">
    <head>
        <meta charset="UTF-8">
        <title>Title</title>
    </head>
    <body>
        <p th:text="'안녕하세요 ' + ${data}"></p>
    </body>
    </html>
    ```
    

스프링 내부 동작 과정을 알아보자

![스크린샷 2024-03-19 오후 6.14.28.png](1%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A9%E1%84%8C%E1%85%A6%E1%86%A8%E1%84%90%E1%85%B3%20%E1%84%92%E1%85%AA%E1%86%AB%E1%84%80%E1%85%A7%E1%86%BC%20%E1%84%89%E1%85%A5%E1%86%AF%E1%84%8C%E1%85%A5%E1%86%BC%2097130d07dbcf4bd19c9e8332bfe2f699/%25E1%2584%2589%25E1%2585%25B3%25E1%2584%258F%25E1%2585%25B3%25E1%2584%2585%25E1%2585%25B5%25E1%2586%25AB%25E1%2584%2589%25E1%2585%25A3%25E1%2586%25BA_2024-03-19_%25E1%2584%258B%25E1%2585%25A9%25E1%2584%2592%25E1%2585%25AE_6.14.28.png)

1. 웹 브라우저에서 `[localhost:8080/hello`로](http://localhost:8080/hello로) GET 요청 보낸다
2. 스프링 내부에 `hello`로 `GET` 매핑된 컨트롤러 존재 확인
3. 존재한다면 해당 컨트롤러를 실행시킨다.
4. 컨트롤러에서 리턴 값으로 반환한 문자열을 이용해 ViewResolver가 화면을 찾아서 처리한다.
    - 스프링부트 템플릿엔진 기본: viewName 매핑
    - resources: `templates/{ViewName}.html`

# 빌드하고 실행하기

빌드 명령어는 다음과 같다.

```bash
# cd $(프로젝트 경로)
./gradlew build # 빌드 명령어
java -jar `build/libs/hello-spring-0.0.1-SNAPSHOT.jar` #서버 실행
```

<aside>
💡 빌드가 잘 안되는 경우, `./gradlew clean build` 로 기존 빌드 내역 삭제 후 재실행

</aside>