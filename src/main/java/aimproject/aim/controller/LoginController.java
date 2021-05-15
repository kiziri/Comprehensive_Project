package aimproject.aim.controller;

import aimproject.aim.config.SessionConfig;
import aimproject.aim.model.Member;
import aimproject.aim.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginForm", new LoginForm());

        return "page/login_page";
    }

    @PostMapping("/login")
    public String login(@Valid LoginForm form, Model model, BindingResult result, HttpServletRequest request) {



        if(result.hasErrors()) {
            return "page/login_page";
        }

        log.info(form.getMemberId());
        Member member = memberService.findByLoginInfo(form.getMemberId(), form.getMemberPw());
        log.info("1 : " + member);
        log.info("1 : " + member.getMemberId());
        log.info("1 : " + member.getName());
        log.info("1 : " + member.getNickname());

        String id = (String) request.getParameter("member");
        System.out.println(id);
        boolean isLoggedIn = memberService.LoginMember(form.getMemberId(), form.getMemberPw());
        if(id!=null) {
            String userId = SessionConfig.getSessionidCheck("member_id", id);
            System.out.println(id + " : " +userId);
            if (isLoggedIn) {
                HttpSession httpSession = request.getSession(true);
                httpSession.setAttribute("member", member);
                return "redirect:/";
            }
        }else {
            if (isLoggedIn) {
                HttpSession httpSession = request.getSession(true);
                httpSession.setAttribute("member", member);
                return "redirect:/";
            }
        }
        return "redirect:/page/login_page";
    }

    @GetMapping("/logout")
    public String logout(SessionStatus sessionStatus, HttpServletRequest request) {
        HttpSession httpSession = request.getSession(false);
        if(httpSession!=null){
            httpSession.invalidate();
        }
        //서비스 멤버 삭제
        sessionStatus.setComplete();
        return "redirect:/";//메인으로 이동
    }
}
