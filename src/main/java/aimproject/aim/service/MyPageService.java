package aimproject.aim.service;

import aimproject.aim.model.AnalysisHistory;
import aimproject.aim.model.Image;
import aimproject.aim.model.Member;
import aimproject.aim.repository.AnalysisHistoryRepository;
import aimproject.aim.repository.ImageRepository;
import aimproject.aim.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyPageService {

    private final AnalysisHistoryRepository analysisHistoryRepository;
    private final MemberRepository memberRepository;
    private final ImageRepository imageRepository;

    /**
     * 회원 기록 저장
     */
    public void save(String memberId, Long imageId) {

        // 회원 정보 및 이미지 정보 조회
        Member member = memberRepository.findOne(memberId);
        Image image = imageRepository.findOne(imageId);

        AnalysisHistory analysisHistory = AnalysisHistory.createHistory(member, image);


        analysisHistoryRepository.save(analysisHistory);
    }

    /**
     * 회원 기록 전체 조회
     */
    public List<AnalysisHistory> findAll() {
        return analysisHistoryRepository.findAll();
    }

    /**
     * 회원별 회원의 기록 전체 조회
     */
    public List<AnalysisHistory> findAllPerMember(String memberId) {
        return analysisHistoryRepository.findAllPerMember(memberId);
    }
}
