package hello.login.web.login;

import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import hello.login.web.session.SessionManager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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

    //@PostMapping
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
         * NOTE: 쿠키로 로그인 구현
         *      응답에 로그인 한 사람의 정보(아이디)를 쿠키 형태로 담아주었다.
         *      브라우저가 닫히기 전까지 쿠키(아이디)를 계속 서버에 전달
         *      페이지에 로그인 정보, 즉 누가 로그인 했는지 보여줄 수 있다.

         * NOTE: 문제점
         *      쿠키는 클라이언트 단에서 쉽게 변경 가능
         *      예측 가능한 정보 담겨 있음 => 쉽게 개인정보 탈취 가능

         * NOTE: 대안
         *      쿠키에 중요한 값(아이디) 노출 x & 예측 불가능 한 임의의 토큰 값 사용
         *      서버에서 세션을 이용해 토큰 - 사용자 id 매핑하여 사용
         *      토큰의 만료 시간 설정(30분 정도) & 해킹 의심시 토큰 삭제 => 유출되더라도 사용 불가
         */
        //세션쿠키(시간 부여 x 쿠키) 생성
        Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
        //응답에 직접 넣어준다
        response.addCookie(idCookie);
        return "redirect:/";
    }

    private final SessionManager sessionManager;
    @PostMapping
    public String loginV2(@Valid @ModelAttribute LoginForm loginForm, BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }
        Member loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
        if (loginMember == null){
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
        }

        // 세션 생성
        sessionManager.createSession(loginMember, response);
        return "redirect:/";
    }
}
