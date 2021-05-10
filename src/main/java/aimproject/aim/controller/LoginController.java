package aimproject.aim.controller;

import aimproject.aim.model.Member;
import aimproject.aim.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@SessionAttributes("member")
public class LoginController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("memberForm", new MemberForm());

        return "page/login_page";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginForm form, Model model, BindingResult result) {

        if (result.hasErrors()) {
            return "page/login_page";
        }

        Member member = new Member();
        member.setMemberId(form.getMemberId());
        member.setMemberEmail(form.getEmail());
        member.setMemberPw(form.getPassword());

        boolean isLoggedIn = memberService.LoginMember(member);
        if(isLoggedIn) {
            model.addAttribute("memberForm", form);
            return "redirect:/";
        }

        return "redirect:/page/join_page";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {

        //서비스 멤버 삭제
        HttpSession session = request.getSession();
        session.invalidate();//세션 삭제
        return "redirect:/";//메인으로 이동
    }
}
