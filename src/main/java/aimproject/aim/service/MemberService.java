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
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);

        return member.getMemberId();
    }

    /**
     * 회원 중복 가입 절차
     */
    public void validateDuplicateMember(Member member) {
        // Exception
        List<Member> findMembers = memberRepository.findByEmail(member.getMemberEmail());

        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 로그인
     */
    public boolean LoginMember(String id, String pass){
        List<Member> findMembers = memberRepository.findByEmail(id);
        if (!findMembers.isEmpty()) {
            //존재하는 회원이라면
            //비밀번호가 유효한지 확인
            //유효하다면 true를 반환
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
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
