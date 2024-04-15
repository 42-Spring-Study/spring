package me.staek;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/basic")
public class BasicController {

    @GetMapping("/text-basic")
    public String TextBasic(Model model) {
        model.addAttribute("data", "hello thymeleaf");
//        model.addAttribute("data", "hello <b>thymeleaf</b>");
        return "basic/text-basic";
    }

    @GetMapping("/text-unescaped")
    public String TextUnescaped(Model model) {
        model.addAttribute("data", "hello <b>thymeleaf</b>");
        return "basic/text-unescaped";
    }
}
