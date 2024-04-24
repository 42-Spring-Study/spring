package me.staek.itemservice.web.login;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import me.staek.itemservice.domain.login.LoginService;
import me.staek.itemservice.domain.member.Member;
import me.staek.itemservice.domain.member.MemberRepository;
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
    @GetMapping("/")
    public String homeLogin2(HttpServletRequest request, Model model) {
        Member member = (Member)sessionManager.getSession(request);
        if (member == null) {
            return "home";
        }
        model.addAttribute("member", member);
        return "loginedHome";
    }
}
