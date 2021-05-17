package aimproject.aim.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import aimproject.aim.model.Member;
import aimproject.aim.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Iterator;
import java.util.List;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private MemberService memberService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession();
        Member member = (Member) session.getAttribute("member");

        if (null == member) {
            if (request.getMethod().equals("POST") && request.getRequestURI().equals("/member")) {
                return true;
            }

            response.sendRedirect("/login");
            return false;
        } else {
            List<Member> memberList = memberService.findMembers();

            if (null != memberList) {
                Iterator<Member> iterator = memberList.iterator();
                while (iterator.hasNext()) {
                    Member targetMember = iterator.next();

                    if ((targetMember.getMemberId()).equals(member.getMemberId())) {
                        return true;
                    }
                }
            } else {
                response.sendRedirect("/error/500");
                return false;
            }
        }
        return false;
    }
}