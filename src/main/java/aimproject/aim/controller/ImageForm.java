package aimproject.aim.controller;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class ImageForm {

    private String memberId;

    private String imageName;

    private String imagePath;

    private LocalDateTime imageDate;
}
