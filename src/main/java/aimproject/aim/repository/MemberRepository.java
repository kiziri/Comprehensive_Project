package aimproject.aim.repository;

import aimproject.aim.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void save(Member member) {       // 회원가입
        em.persist(member);
    }

    public Member findOne(Long memberId) {
        return em.find(Member.class, memberId);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public Member findByEmail(String memberEmail) {
        return em.createQuery("select m from Member m where m.memberEmail = :memberEmail", Member.class)
                .setParameter("memberEmail", memberEmail).getSingleResult();
    }

    public List<Member> findByEmailForDuplicate(String memberEmail) {
        return em.createQuery("select m from Member m where m.memberEmail = :memberEmail", Member.class)
                .setParameter("memberEmail", memberEmail).getResultList();
    }
}
