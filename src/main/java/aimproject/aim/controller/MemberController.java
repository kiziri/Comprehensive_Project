package aimproject.aim.controller;

import aimproject.aim.model.Member;
import aimproject.aim.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/join")
    public String join(Model model) {
        model.addAttribute("memberForm", new MemberForm());

        return "page/join_page";
    }

    @PostMapping("/join")
    public String join(@Valid MemberForm form, BindingResult result) {

        if (result.hasErrors()) {
            return "page/join_page";
        }

        Member member = new Member();
        member.setMemberEmail(form.getEmail());
        member.setMemberPw(form.getPassword());
        member.setName(form.getName());
        member.setNickname(form.getNickname());
        member.setTelNumber(form.getTelNumber());
        member.setAddress(form.getAddress());

        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "page/login_page";
    }

}
