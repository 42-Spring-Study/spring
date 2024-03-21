# 섹션 5. 회원 관리 예제 - 웹 MVC 개발

```python
    @GetMapping("/")
    public String home() {
        return "home";
    }
```

위와 같은 형식으로 root경로로 맵핑이된 `Controller` 가 존재한다면 그 이름으로 지정이 된 파일을 찾고,

`root`경로로 맵핑된 `controller`가 존재하지않는다면 `index.html` 파일을 찾고 해당 파일을 보여준다. ( 우선순위가 존재 )

## html class 바인딩

```python
@PostMapping("/members/new")
public  String create(MemberForm form) {
    Member member = new Member();
    member.setName(form.getName());

    memberService.join(member);

    return "redirect:/";
}
```

```python
<form action="/members/new" method="post">
    <div class="form-group">
        <label for="name">이름</label>
        <input type="text" id="name" name="name" placeholder="이름을 입력하세요">
    </div>
    <button type="submit">등록</button> 
</form>
```

위와 같이 `name`을 저장하는 `form html`과 그것의 `post`내용을 받는 `controller`가 있을때 `form`요청을 `"/members/new"` 해당 url로 보내게 되면

```python
public class MemberForm {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

해당 class내부에 있는 `private String name`에 자동으로 저장이 된다고한다.

`spring mvc`가 기본으로 지원해주는 기능으로 웹의 파라미터에 `name`이라는 이름이 있으면 이 이름을 보고 `spring mvc`가 `setName`을 호출한다고한다.

위에 코드에서 `setName`이 있기때문에 해당 과정이 가능한것이라 `setName`함수를 없애버리면 `private String name`에 값을 저장할 수 없게 된다.

( setName을 주석처리한 후에 돌려보면 `form.getName() = null` 이와 같이 `null`이 나온다 )

`java bean property` 규약으로 `getter, setter` 메서드의 이름을 맞춰야한다고한다.
( `name`이면 `getName(return this.name;), setName(.. this.name = param )` )

```python
return "redirect:/";
```

위와 같은 형식으로의 반환을 넣으면 `redirect`이 가능하다고한다.

## th:each

```python
<tr th:each="member : ${members}">
    <td th:text="${member.id}"></td>
    <td th:text="${member.name}"></td>
</tr>
```

`tr문법`으로 `th:each` 디렉티브에 의해서 `${members}`가 컬렉션의 각 요소에 대해 반복이 된다고한다.