##### 프로젝트 생성
스프링부트 스타터
https://start.spring.io/
영상에서는 2.x버전사용 
현재는 3.x 버전

##### 라이브러리 살펴보기
Gradle 사용
스프링부트 스타터
thymeleaf
slf4j 로깅 라이브러리
junit

##### view환경설정
스프링 생태계는 거대하기때문에 필요한것을 찾는 능력이 중요
thymeleaf 사용하여 hellowold 찍기

1. 패키지 생성
2. 컨트롤러 클래스 생성
	1. @Controller 사용
	2. @GetMapping("주소")
	3. Model 사용
		1. Model model
		2. model.ddAttribute("이름", "깂");
		3. return model; 
3. thymeleaf 에서 model에 입력한 값 받기 
	1. ${이름}
##### 빌드하고 실행하기
터미널에서 Gradle 빌드 후 실행
1. ./gradlew build
2. java -jar 빌드된파일이름.jar