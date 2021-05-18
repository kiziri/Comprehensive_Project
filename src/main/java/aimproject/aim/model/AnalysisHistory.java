package aimproject.aim.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "analysis_history")
@Getter @Setter
public class AnalysisHistory {

    @Id @GeneratedValue
    @Column(name = "history_id")
    private Long historyId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;          // 분석을 요청한 회원

    @OneToOne(cascade = CascadeType.ALL, fetch = LAZY)
    @JoinColumn(name = "image_id")
    private Image image;            // 분석 기록 당 이미지 하나 이미지 정보

    private LocalDateTime historyDate;     // 분석 요청 시간


    // 연관 관계 메서드 //
    public void setMember(Member member) {
        this.member = member;
        member.getHistories().add(this);
    }

    public void setImage(Image image) {
        this.image = image;
        image.setAnalysisHistory(this);
    }

    // 생성 메서드 //
    public static AnalysisHistory createHistory(Member member, Image image) {
        AnalysisHistory analysisHistory = new AnalysisHistory();
        analysisHistory.setMember(member);
        analysisHistory.setImage(image);

        analysisHistory.setHistoryDate(LocalDateTime.now());

        return analysisHistory;
    }
}
