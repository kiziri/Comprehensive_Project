package aimproject.aim.controller;

import aimproject.aim.model.Member;
import aimproject.aim.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("memberForm", new MemberForm());

        return "page/login_page";
    }

    @GetMapping("/members/login")
    public String login(LoginForm form, @RequestParam(value = "memberEmail") String memberEmail,
                        @RequestParam(value = "memberPw") String memberPw, HttpServletRequest request) {

        if(memberService.LoginMember(memberEmail,memberPw)){
            //회원가입 결과가 참이라면
            HttpSession session = request.getSession();
            session.setAttribute("loginCheck",true);//세션을 생성
            session.setAttribute("memberEmail", memberEmail);//세션의 아이디를 저장, 혹은 아이디에 맞는 닉네임도 같이 저장
            return "redirect:/";//메인으로 이동
        }
        else{

        }
        return "page/login_page";//실패시 다시 로그인페이지로 이동
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {

        //서비스 멤버 삭제
        HttpSession session = request.getSession();
        session.invalidate();//세션 삭제
        return "redirect:/";//메인으로 이동
    }

}
