package aimproject.aim.repository;

import aimproject.aim.model.AnalysisHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class AnalysisHistoryRepository {

    private final EntityManager em;

    /**
     * 회원의 검색 기록 정보 저장
     */
    public void save(AnalysisHistory analysisHistory) {
        em.persist(analysisHistory);
        log.info("" + analysisHistory.getHistoryId());
    }

    /**
     * 기록 정보 전제 조회
     */
    public List<AnalysisHistory> findAll() {
        return em.createQuery("select a from AnalysisHistory a", AnalysisHistory.class).getResultList();
    }

    /**
     * 기록 정보, 회원별 전체 조회
     */
    public List<AnalysisHistory> findAllPerMember(String memberId) {
        return em.createQuery("select a from AnalysisHistory a where a.member.memberId =:memberId")
                .setParameter("memberId", memberId)
                .getResultList();
    }

    /**
     * 기록 정보 단건 조회
     */
    public AnalysisHistory findOne(Long historyId) {
        return em.find(AnalysisHistory.class, historyId);
    }

    /**
     * 기록 정보, 날짜와 단건 조회
     */
    public List<AnalysisHistory> findByAnalysisDate(String memberId, LocalDateTime historyDate) {
        return em.createQuery("select a from AnalysisHistory a where a.member.memberId =:memberId " +
                "and a.historyDate =:historyDate", AnalysisHistory.class)
                .setParameter("memberId", memberId)
                .setParameter("historyDate", historyDate)
                .getResultList();
    }
}
