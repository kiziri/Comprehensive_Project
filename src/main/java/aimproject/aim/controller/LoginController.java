package aimproject.aim.controller;

import aimproject.aim.model.Member;
import aimproject.aim.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@SessionAttributes("member")
@Slf4j
public class LoginController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginForm", new LoginForm());

        return "page/login_page";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form, Model model, BindingResult result) {

        if(result.hasErrors()) {
            return "page/login_page";
        }

        log.info(form.getMemberEmail());
        Member member = memberService.findByEmail(form.getMemberEmail());
        log.info("1 : " + member);
        log.info("1 : " + member.getMemberId());
        log.info("1 : " + member.getMemberEmail());
        log.info("1 : " + member.getNickname());

        boolean isLoggedIn = memberService.LoginMember(member);
        if(isLoggedIn) {
            model.addAttribute("member", member);
            return "redirect:/";
        }

        return "redirect:/page/login_page";
    }

    @GetMapping("/logout")
    public String logout(SessionStatus sessionStatus) {
        //서비스 멤버 삭제
        sessionStatus.setComplete();
        return "redirect:/";//메인으로 이동
    }
}
