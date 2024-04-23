package me.staek.itemservice.web.login;

import lombok.RequiredArgsConstructor;
import me.staek.itemservice.domain.login.LoginService;
import me.staek.itemservice.domain.member.Member;
import me.staek.itemservice.domain.member.MemberRepository;
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
    @GetMapping("/")
    public String home2(@CookieValue(name = "memberId", required = false) Long memberId, Model model) {

        if (memberId == null) {
            return "home";
        }

        Member member = memberRepository.findById(memberId);
        model.addAttribute("member", member);
        return "loginedHome";
    }

}
