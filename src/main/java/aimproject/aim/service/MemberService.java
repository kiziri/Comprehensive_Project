package aimproject.aim.service;

import aimproject.aim.model.Member;
import aimproject.aim.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    
    private final MemberRepository memberRepository;

    /**
     * 회원 가입
     */
    @Transactional
    public String join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);

        return member.getMemberId();
    }

    /**
     * 회원 중복 가입 절차
     */
    public void validateDuplicateMember(Member member) {
        // Exception
        List<Member> findMembers = memberRepository.findByEmailForDuplicate(member.getMemberId());

        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 로그인
     */
    public boolean LoginMember(Member member){

        Member findMember = memberRepository.findOne(member.getMemberId());
        if (findMember.getMemberPw().equals(member.getMemberPw())) {
            return true;
        }
        return false;
    }


    /**
     * 회원 전체 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /**
     * 회원 단건 조회
     */
    public Member findOne(String memberId) {
        return memberRepository.findOne(memberId);
    }

    public Member findById(String memberId) {
        return memberRepository.findById(memberId);
    }

    public Member findByLoginInfo(String memberId, String memberPw) {
        return memberRepository.findByLoginInfo(memberId, memberPw);
    }
}
