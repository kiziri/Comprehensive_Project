package aimproject.aim.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberController {



    @GetMapping("/join")
    public String join(Model model) {
        model.addAttribute("memberForm", new MemberForm());

        return "page/join_page";
    }

    @PostMapping("/join")
    public String join(@Valid MemberForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "page/login_page";
    }

}
