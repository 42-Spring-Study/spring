package hello.login.web.login;

import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {
    //final 생략 시 생성자 주입 x -> NullPointException 발생
    private final LoginService loginService;

    @GetMapping
    public String loginForm(@ModelAttribute("loginForm") LoginForm loginForm) {
        return "login/loginForm";
    }

    @PostMapping
    public String login(@Valid @ModelAttribute LoginForm loginForm, BindingResult result, HttpServletResponse response) {
        if (result.hasErrors()) {
            return "login/loginForm";
        }
        log.info("loginForm={}", loginForm);
        Member loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
        log.info("login? {}", loginMember);

        //로그인 실패 시
        if (loginMember == null) {
            //loginFail: 현 시점에 존재하지 않는 Message
            result.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        //로그인 성공 시

        /**
         * NOTE: 응답에 로그인 한 사람의 정보(아이디)를 쿠키 형태로 담아주었다.
         *      브라우저가 닫히기 전까지 쿠키(아이디)를 계속 서버에 전달
         *      페이지에 로그인 정보, 즉 누가 로그인 했는지 보여줄 수 있다.
         */
        //세션쿠키(시간 부여 x 쿠키) 생성
        Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
        //응답에 직접 넣어준다
        response.addCookie(idCookie);
        return "redirect:/";
    }
}
