package me.staek.itemservice.web.login;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.staek.itemservice.domain.login.LoginService;
import me.staek.itemservice.domain.member.Member;
import me.staek.itemservice.web.session.SessionConst;
import me.staek.itemservice.web.session.SessionManager;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
//    @PostMapping("/login")
    public String login2(@Validated @ModelAttribute LoginDto loginDto
            , BindingResult br
            , HttpServletResponse response) {
        if (br.hasErrors()) {
            return "login/loginForm";
        }

        Member logined = loginService.login(loginDto.getLoginId(), loginDto.getPassword());

        if (logined == null) {
            br.reject("loginFail");
            return "login/loginForm";
        }
        /**
         * 세션에 회원데이터 저장
         */
        sessionManager.createSession(logined, response);
        return "redirect:/";
    }

    /**
     * HttpSession 를 이용해서 세션을 관리
     * - 무조건 home화면으로 이동한다.
     */
//    @PostMapping("/login")
    public String login3(@Validated @ModelAttribute LoginDto form, BindingResult
            br, HttpServletRequest request) {
        if (br.hasErrors()) {
            return "login/loginForm";
        }
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        log.info("login? {}", loginMember);
        if (loginMember == null) {
            br.reject("loginFail");
            return "login/loginForm";
        }
        /**
         * 세션이 있으면 있는 세션 반환, 없으면 신규 세션 생성
         */
        HttpSession session = request.getSession();
//        session.setMaxInactiveInterval(1800); // 1800 sec
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        return "redirect:/";
    }


    /**
     * 로그인 이후 redirect 처리
     * - 이미 로그인이 되어있거나 로그인이전에 요청했던 url에 로그인체크 성공 후 redirect한다.
     */
    @PostMapping("/login")
    public String login4(
            @Validated @ModelAttribute LoginDto form, BindingResult br,
            @RequestParam(defaultValue = "/") String redirectURL, HttpServletRequest request) {

        if (br.hasErrors()) {
            return "login/loginForm";
        }
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        log.info("login? {}", loginMember);
        if (loginMember == null) {
            br.reject("loginFail");
            return "login/loginForm";
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        /**
         * redirectURL 적용
         */
        return "redirect:" + redirectURL;
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
//    @PostMapping("/logout")
    public String logout2(HttpServletRequest request) {
        sessionManager.expire(request);
        return "redirect:/";
    }

    private static void clearCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    @PostMapping("/logout")
    public String logout3(HttpServletRequest request) {
        // 세션을 삭제
        HttpSession session = request.getSession(false);
        if (session != null)
            session.invalidate();
        return "redirect:/";
    }
}
