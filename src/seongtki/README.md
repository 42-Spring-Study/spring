# Source

강의관련 코드정리



## Section



#### [1. hello-core]

#### [2. hello-spring]

#### [3. servlet-mvc]

#### [4. spring-mvc]

#### [5. spring-thymeleaf]



#### [6. thymeleaf-grammar](./thymeleaf-grammar)

- MVC2 섹션1 타임리프 - 기본 기능
  - 타임리프 기본문법 작성
  - 디렉토리: `./`

#### [7. item-service]()

- MVC1 섹션7 웹페이지 만들기
  - 상품 등록,상세,수정,목록조회 서비스를 만들고 타임리프로 표현하는 사이트 작성.
  
  - ~~~
    me.staek.itemservice.web.BasicItemController
    ~~~
  
- MVC2 섹션2 스프링 통합과 폼
  - 입력폼, 체크박스, 라디오버튼, 셀렉트 박스
  
  - ~~~
    me.staek.itemservice.web.FormItemController
    ~~~
  
- MVC2 섹션3 메시지, 국제화
  - 스프링 properties에서 언어를 설정하고, 타임리프에서 표현하는 방법 작성.
  
  - ~~~
    me.staek.itemservice.web.FormItemController
    ~~~
  
- MVC2 섹션4 Validation
  - 상품서비스 서버검증로직 작성
    1. 컨트롤러에 직접 작성
    2. BindingResult 이용하여 작성
    
  - ~~~
    me.staek.itemservice.web.validation.ValidationItemControllerV1
    me.staek.itemservice.web.validation.ValidationItemControllerV2
    ~~~
  
- MVC2 섹션5 Bean Validation
  - Spring Bean Validation을 이용하여 서버검증로직 작성
  
  - ```
    me.staek.itemservice.web.validation.ValidationItemControllerV3
    me.staek.itemservice.web.validation.ValidationItemControllerV4
    me.staek.itemservice.web.validation.ValidationItemControllerAPI
    ```





