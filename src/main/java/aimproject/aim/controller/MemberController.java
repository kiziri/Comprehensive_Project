package aimproject.aim.controller;

import aimproject.aim.model.AnalysisHistory;
import aimproject.aim.model.Member;
import aimproject.aim.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

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
        member.setMemberId(form.getMemberId());
        member.setMemberPw(form.getMemberPw());
        member.setName(form.getName());
        member.setNickname(form.getNickname());
        member.setTelNumber(form.getTelNumber());
        member.setAddress(form.getAddress());

        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/histories")
    public String analysisHistory(Model model, HttpServletRequest request) {
        // 세션 받아오기
        HttpSession session = request.getSession();

        // 요청한 세션의 정보를 가져와 회원의 아이디 저장
        Member member = (Member)session.getAttribute("member");
        String memberId = member.getMemberId();

        model.addAttribute("memberId", memberId);

        return "redirect:/histories/"+memberId;
    }

    @GetMapping("/histories/{memberId}")
    public String analysisHistory(@PathVariable String memberId, Model model, HttpServletRequest request) {
        // 세션 받아오기
        HttpSession session = request.getSession();

        // 요청한 세션의 정보를 저장
        Member member = (Member)session.getAttribute("member");
        
        // 이미지 분석 기록 가져오기
        List<AnalysisHistory> analysisHistories = memberService.findAllPerMember(memberId);

        model.addAttribute("histories", analysisHistories);

        return "page/history_page";
    }
}
