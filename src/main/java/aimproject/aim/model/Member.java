package aimproject.aim.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private String memberId;

    @Column(name = "member_pw")
    private String memberPw;

    @Column(name = "member_name")
    private String name;

    @Column(name = "member_nickname")
    private String nickName;

    @Column(name = "member_telnumber")
    private String telNumber;

    @Column(name = "member_address")
    private String address;

    @OneToMany(mappedBy = "member")
    private List<AnalysisHistory> histories = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    private Image image;
}
