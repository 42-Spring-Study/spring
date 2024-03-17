# 05. 회원 관리 예제 - 웹 MVC 개발



브라우저 url 요청을 컨트롤러가 받는 부분, 클라이언트로 view를 전달하는 부분을 작성한다.

url 요청 의도에 대해 매칭되는 HTTP Method와 함께 컨트롤러를 작성하고

Model 데이터를 html에 템플릿엔진을 이용해 작성 후 클라이언트로 전달되도록 html파일을 작성한다.





## 회원 웹 기능 - 홈 화면 추가

##### 홈 화면 생성

- 회원가입 이동버튼
- 회원목록 이동버튼



~~~java
@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "home";
    }
}
~~~

##### 회원 관리용 홈

~~~html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<body>
<div class="container">
    <div>
        <h1>Hello Spring</h1>
        <p>회원 기능</p>
        <p>
            <a href="/members/new">회원 가입</a>
            <a href="/members">회원 목록</a>
        </p>
    </div>
</div> <!-- /container -->
</body>
</html>
~~~







## 회원 웹 기능 - 등록

회원 등록폼을 이용해서 회원을 등록하고 홈 화면으로 이동하는 로직 작성



##### 회원 등록 폼 컨트롤러

- Get: `/members/new` 
- 클라이언트에 회원등록 폼(templates/members/createMemberForm.html) 전달

~~~java
@Controller
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        System.out.println(memberService.getClass());
        this.memberService = memberService;
    }

    @GetMapping(value = "/members/new")
    public String createForm() {
        return "members/createMemberForm";
    }
    ...
~~~



##### 회원 등록 폼 HTML ( resources/templates/members/createMemberForm )

~~~html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<body>
<div class="container">
    <form action="/members/new" method="post">
        <div class="form-group">
            <label for="name">이름</label>
            <input type="text" id="name" name="name" placeholder="이름을 입력하세요">
        </div>
        <button type="submit">등록</button>
    </form>
</div> <!-- /container -->
</body>
</html>
~~~



##### 웹 등록 화면에서 데이터를 전달 받을 폼 객체

- 흔히 dto 라고 한다.

~~~java
public class MemberForm {
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
~~~



##### 회원 컨트롤러에서 회원을 실제 등록하는 기능

- Post: `/members/new`
- html으로부터 전달된 MemberForm 객체의 scope ?
- 홈화면으로 redirect 한다.

~~~java
@PostMapping(value = "/members/new")
public String create(MemberForm form) {
    Member member = new Member(form.getName());
    memberService.join(member);
    return "redirect:/";
}
~~~





## 회원 웹 기능 - 조회



##### 회원 컨트롤러에서 조회 기능

- Get: `/members`
- 템플릿엔진에의해 model 데이터를 html에 작성 후 클라이언트에 회원목록(template/members/memberList.html) 전달
  - Model: request scope 

~~~java
@GetMapping(value = "/members")
public String list(Model model) {
    List<Member> members = memberService.findMembers();
    model.addAttribute("members", members);
    return "members/memberList";
}
~~~



##### 회원 리스트 HTML

~~~html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<body>
<div class="container">
    <div>
        <table>
            <thead>
            <tr>
                <th>#</th>
                <th>이름</th>
            </tr>
            </thead>
            <tbody>
            <tr th:each="member : ${members}">
                <td th:text="${member.id}"></td>
                <td th:text="${member.name}"></td>
            </tr>
            </tbody>
        </table>
    </div>
</div> <!-- /container -->
</body>
</html>
~~~





## 마치며

Servlet 에서 HTTP Request 에 따라 Spring Controller method로 전달되는 로직과

브라우저로 전달 될 html이 작성될 때 thymeleaf 동작원리에 대해 MVC 1,2 편에서 좀더 자세히 학습할 것이다.





















