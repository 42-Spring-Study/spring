package me.staek.itemservice.web.login;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.staek.itemservice.domain.login.LoginService;
import me.staek.itemservice.domain.member.Member;
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

    @GetMapping("/login")
    public String loginForm(@ModelAttribute LoginDto loginDto) {
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute LoginDto loginDto, BindingResult br) {
        if (br.hasErrors()) {
            return "login/loginForm";
        }

        Member logined = loginService.login(loginDto.getLoginId(), loginDto.getPassword());

        if (logined == null) {
            br.reject("loginFail", "아이디 혹은 비번이 틀렸습니다.");
            return "login/loginForm";
        }
        return "redirect:/";
    }
}
