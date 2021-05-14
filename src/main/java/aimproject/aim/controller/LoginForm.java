package aimproject.aim.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class LoginForm {

    @NotEmpty(message = "회원 이메일는 필수 입니다.")
    private String memberId;

    @NotEmpty(message = "회원 비밀번호는 필수 입니다.")
    private String memberPw;
}
