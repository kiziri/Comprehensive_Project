package aimproject.aim.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter
public class Image {

    @Id @GeneratedValue
    @Column(name = "history_id")
    private Long id;

    private String imageName;

    private Long happyResult;
    private Long sadResult;
    private Long neutralResult;
    private Long angryResult;
    private Long fearResult;
    private Long surpriseResult;
}
