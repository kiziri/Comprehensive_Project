package aimproject.aim.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Getter @Setter
public class AnalysisHistory {

    @Id @GeneratedValue
    @Column(name = "image_id")
    private Long id;


    private String imageName;


    private String imagePath;


    private Date date;


    private String memberId;
}
