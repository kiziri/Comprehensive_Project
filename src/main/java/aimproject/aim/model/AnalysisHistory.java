package aimproject.aim.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class AnalysisHistory {

    @Id @GeneratedValue
    @Column(name = "history_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;          // 분석을 요청한 회원

    @OneToOne(cascade = CascadeType.ALL, fetch = LAZY)
    @JoinColumn(name = "image_id")
    private Image image;            // 분석 기록 당 이미지 하나 이미지 정보

    private LocalDateTime date;     // 분석 요청 시간

}
