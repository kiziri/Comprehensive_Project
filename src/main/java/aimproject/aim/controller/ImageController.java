package aimproject.aim.controller;

import aimproject.aim.model.Image;
import aimproject.aim.model.Member;
import aimproject.aim.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ImageController {

    private final ImageService imageService;

    @RequestMapping("/demo")
    public String demo(Model model) {
        return "page/demo_page";
    }

    @RequestMapping("/result")
    public String result(Model model) {
        return "page/result_page";
    }

    @PostMapping("/demo/analysis")
    public String imageAnalysis(MultipartFile file, Model model, HttpServletRequest request) throws Exception {

        // 요청한 세션의 정보를 가져와 회원의 아이디 저장
        Member member = (Member)request.getSession().getAttribute("member");
        String memberId = member.getMemberId();

        // 파일 속성 출력
        log.info("imageOriginName: " + file.getOriginalFilename());
        log.info("size: " + file.getSize());
        log.info("contentType: " + file.getContentType());

        // UUID 설정된 이미지파일명 설정
        String imageNameByUUID = imageService.setImageNameByUUID(file.getOriginalFilename(), memberId);

        Image image = new Image();
        image.setImageName(imageNameByUUID);
        image.setImageOriginName(file.getOriginalFilename());
        image.setImageDate(LocalDateTime.now());

        Long imageId = imageService.save(memberId, image);
        log.info(""+imageId);

        // 해당 이미지명 및 경로 모델로 전송
        model.addAttribute("savedName", imageNameByUUID);

        // 파일 저장은 서비스단에서 구현
        //File target = new File(directoryPath, imageNameByUUID);  // 파일명과 경로 지정
        //FileCopyUtils.copy(file.getBytes(), target);    // target에 해당하는 파일 생성

        return "redirect:/result/{memberNickname}";
    }



    //프론트 이미지 출력
    @GetMapping(value = "/result/{memberNickname}")
    public void loadImage(Model model, HttpServletRequest request, @PathVariable String memberNickname) {
        
    }
}
