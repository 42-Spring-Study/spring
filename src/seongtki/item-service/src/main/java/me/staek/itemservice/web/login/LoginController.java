package me.staek.itemservice.web.login;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.staek.itemservice.domain.login.LoginService;
import me.staek.itemservice.domain.member.Member;
import me.staek.itemservice.web.session.SessionManager;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
//@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;
    private final SessionManager sessionManager;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute LoginDto loginDto) {
        return "login/loginForm";
    }


    /**
     *
     * 로그인 성공 시 Response 쿠키에 MemberId 세팅
     *
     * 단점: memberid는 보안에 취약 (브라우저에 노출됨)
     */
//    @PostMapping("/login")
    public String login(@Validated @ModelAttribute LoginDto loginDto
                        , BindingResult br
                        , HttpServletResponse response) {
        if (br.hasErrors()) {
            return "login/loginForm";
        }

        Member logined = loginService.login(loginDto.getLoginId(), loginDto.getPassword());

        if (logined == null) {
            br.reject("loginFail", "아이디 혹은 비번이 틀렸습니다.");
            return "login/loginForm";
        }
        response.addCookie(new Cookie("memberId", String.valueOf(logined.getId())));
        return "redirect:/";
    }

    /**
     *
     * 세션에 회원데이터 저장
     */
    @PostMapping("/login")
    public String login2(@Validated @ModelAttribute LoginDto loginDto
            , BindingResult br
            , HttpServletResponse response) {
        if (br.hasErrors()) {
            return "login/loginForm";
        }

        Member logined = loginService.login(loginDto.getLoginId(), loginDto.getPassword());

        if (logined == null) {
            br.reject("loginFail", "아이디 혹은 비번이 틀렸습니다.");
            return "login/loginForm";
        }
        /**
         * 세션에 회원데이터 저장
         */
        sessionManager.createSession(logined, response);
        return "redirect:/";
    }


    /**
     * 로그아웃 시 쿠키초기화
     */
//    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        clearCookie(response, "memberId");
        return "redirect:/";
    }

    /**
     * 로그아웃 시 세션만료
     */
    @PostMapping("/logout")
    public String logout2(HttpServletRequest request) {
        sessionManager.expire(request);
        return "redirect:/";
    }

    private static void clearCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
