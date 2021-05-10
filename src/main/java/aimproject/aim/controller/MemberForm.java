package aimproject.aim.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class MemberForm {

    @NotEmpty(message = "회원 아이디는 필수 입니다.")
    private Long memberId;

    @NotEmpty(message = "회원 이메일는 필수 입니다.")
    private String Email;
    
    @NotEmpty(message = "회원 비밀번호는 필수 입니다.")
    private String password;
    
    @NotEmpty(message = "회원 이름은 필수 입니다.")
    private String name;

    @NotEmpty(message = "회원 닉네임은 필수 입니다.")
    private String nickName;

    @NotEmpty(message = "회원 전화번호는 필수 입니다.")
    private String telNumber;
    
    private String address;
}
