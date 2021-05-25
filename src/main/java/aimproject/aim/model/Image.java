package aimproject.aim.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class Image {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "image_id")
    private Long imageId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(mappedBy = "image", fetch = LAZY)
    private AnalysisHistory analysisHistory;

    private String imageName;

    private String imageOriginName;

    private String imagePath;
    
    private LocalDateTime imageDate;         // 이미지 저장 시간


    // 연관 관계 메서드 //
    public void setMember(Member member) {
        this.member = member;
        member.getImages().add(this);
    }

    // 생성 메서드 //
    public static Image createImage(Image imageInfo, Member member) {
        Image image = new Image();

        image.setMember(member);    // 연관 관계 메서드 호출
        image.setImageName(imageInfo.getImageName());
        image.setImageOriginName(imageInfo.getImageOriginName());
        image.setImagePath(imageInfo.getImagePath());
        image.setImageDate(imageInfo.getImageDate());

        return image;
    }
}
