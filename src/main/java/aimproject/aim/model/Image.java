package aimproject.aim.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class Image {

    @Id @GeneratedValue
    @Column(name = "image_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member")
    private Member member;

    @OneToOne(mappedBy = "image", fetch = LAZY)
    private AnalysisHistory analysisHistory;

    private String imageName;

    private String imagePath;
    
    private LocalDateTime date;         // 이미지 저장 시간

}
