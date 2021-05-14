package aimproject.aim.service;

import aimproject.aim.model.Member;
import aimproject.aim.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired EntityManager em;
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void 로그인확인() throws Exception {

        // given
        Member member = createMember();

        // when
        boolean isLoggedIn = memberService.LoginMember(member.getMemberId(), member.getMemberPw());


        // then
        assertEquals("입력한 비밀번호와 조회한 비밀번호가 일치해야 한다.", isLoggedIn, true);

    }

    private Member createMember() {
        Member member = new Member();
        member.setName("member1");
        member.setMemberId("kim");
        member.setMemberPw("123");
        member.setNickname("kim");
        member.setTelNumber("123-1234-1234");
        member.setAddress("서울가123-123");
        em.persist(member);
        return member;
    }
}