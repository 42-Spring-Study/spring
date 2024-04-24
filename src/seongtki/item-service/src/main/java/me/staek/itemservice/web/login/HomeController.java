package me.staek.itemservice.web.login;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import me.staek.itemservice.argumentresolver.Login;
import me.staek.itemservice.domain.login.LoginService;
import me.staek.itemservice.domain.member.Member;
import me.staek.itemservice.domain.member.MemberRepository;
import me.staek.itemservice.web.session.SessionConst;
import me.staek.itemservice.web.session.SessionManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

//    @GetMapping("/")
    public String home() {
        return "home";
    }


    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

//    @GetMapping("/")
    public String home2(@CookieValue(name = "memberId", required = false) Long memberId, Model model) {

        if (memberId == null) {
            return "home";
        }

        Member member = memberRepository.findById(memberId);
        model.addAttribute("member", member);
        return "loginedHome";
    }

    /**
     * 세션 관리자에 저장된 회원 정보 조회
     */
//    @GetMapping("/")
    public String homeLogin2(HttpServletRequest request, Model model) {
        Member member = (Member)sessionManager.getSession(request);
        if (member == null) {
            return "home";
        }
        model.addAttribute("member", member);
        return "loginedHome";
    }

//    @GetMapping("/")
    public String homeLogin3(HttpServletRequest request, Model model) {
        /**
         * 세션존재 유무 검사
         */
        HttpSession session = request.getSession(false);
        if (session == null)
            return "home";
        /**
         * 세션에 회원데이터 검사
         */
        Member loginMember = (Member)
                session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember == null)
            return "home";

        model.addAttribute("member", loginMember);
        return "loginedHome";
    }

    /**
     * @Login 애노테이션이 있으면 직접 만든 ArgumentResolver 가 동작해서 자동으로 세션에 있는
     * 로그인 회원을 찾아서 객체에 주입된다.
     */
    @GetMapping("/")
    public String homeLogin3ArgumentResolver(@Login Member loginMember, Model model) {
        //세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            return "home";
        }
        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginedHome";
    }
}
