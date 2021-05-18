package aimproject.aim.repository;

import aimproject.aim.model.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ImageRepository {

    private final EntityManager em;

    /**
     * 회원의 요청 이미지 정보 저장
     */
    public void save(Image image) {
        em.persist(image);
    }

    /**
     * 이미지 건별 단건 조회
     */
    public Image findOne(Long imageId) {
        return em.find(Image.class, imageId);
    }

    /**
     * 이미지 정보 전체 조회
     */
    public List<Image> findAll() {
        return em.createQuery("select i from Image i", Image.class).getResultList();
    }

    /**
     * 회원 요청 이미지, 이미지아이디와 단건 조회
     */
    public Image findByImageId(String memberId, Long imageId) {
        return em.createQuery("select i from Image i where i.member.memberId =:memberId " +
                "and i.imageId = :imageId", Image.class)
                .setParameter("memberId", memberId)
                .setParameter("imageId", imageId)
                .getSingleResult();
    }

    /**
     * 회원 요청 이미지, 이미지이름과 단건 조회
     */
    public Image findByImageName(String memberId, Long imageId) {
        return em.createQuery("select i from Image i where i.member.memberId =:memberId " +
                "and i.imageId = :imageId", Image.class)
                .setParameter("memberId", memberId)
                .setParameter("imageId", imageId)
                .getSingleResult();
    }

    /**
     * 회원 요청 이미지, 이미지 날짜 단건 조회
     */
    public Image findByImageDate(String memberId, LocalDateTime imageDate) {
        return em.createQuery("select i from Image i where i.member.memberId =:memberId " +
                "and i.imageDate = :imageDate", Image.class)
                .setParameter("memberId", memberId)
                .setParameter("imageDate", imageDate)
                .getSingleResult();
    }
}
