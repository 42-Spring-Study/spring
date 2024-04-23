package me.staek.itemservice.domain.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberRepository repository;

    @GetMapping("/add")
    public String addForm(@ModelAttribute Member member) {
        return "addMemberForm";
    }


    @PostMapping("/add")
    public String save(@Validated @ModelAttribute Member member, BindingResult br) {

        if (br.hasErrors()) {
            return "addForm";
        }
        repository.save(member);
        return "redirect:/";
    }
}
