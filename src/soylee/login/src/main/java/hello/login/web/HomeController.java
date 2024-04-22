package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.session.SessionManager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
    private final MemberRepository memberRepository;

    //    @GetMapping("/")
    public String home() {
        return "home";
    }

    //@GetMapping
    public String homeLogin(@CookieValue(name = "memberId", required = false) Long memberId, Model model) {
        if (memberId == null) {
            return "home";
        }

        Member loginMember = memberRepository.findById(memberId);
        if (loginMember == null) {
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    /**
     * NOTE: logout 구현(쿠키)
     *      쿠키 종료 날짜 0으로 지정
     *      세션쿠키이므로 브라우저 종료 시에도 로그아웃 된다.
     */
    //@PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        expireCookie(response, "memberId");
        return "redirect:/";
    }

    private static void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    private final SessionManager sessionManager;

    @GetMapping
    public String homeV2(HttpServletRequest request, Model model) {
        Member member = (Member)sessionManager.getSession(request);
        if (member == null) {
            return "home";
        }

        model.addAttribute("member", member);
        return "loginHome";
    }

    @PostMapping("/logout")
    public String logoutV2(HttpServletRequest request) {
        sessionManager.expire(request);
        return "redirect:/";
    }
}