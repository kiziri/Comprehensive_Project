package aimproject.aim.service;

import aimproject.aim.model.Member;
import aimproject.aim.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {

        // given
        Member member = new Member();
        member.setMemberId("kim");

        // when
        String savedId = memberService.join(member);

        // then
        assertEquals(member, memberRepository.findOne(savedId));

    }
    
    @Test(expected = IllegalStateException.class)
    public void 중복회원가입() throws Exception {

        // given
        Member member1 = new Member();
        member1.setMemberId("kim");

        Member member2 = new Member();
        member2.setMemberId("kim");


        // when
        memberService.join(member1);
        memberService.join(member2);    // 예외 발생 필요

        // then
        fail("예외가 발생해야 한다.");
        
    }
}