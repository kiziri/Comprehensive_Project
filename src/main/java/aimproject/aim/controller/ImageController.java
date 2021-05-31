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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ImageController {

    private final ImageService imageService;

    @RequestMapping("/demo")
    public String demo(Model model) {
        return "page/demo_page";
    }

    /*@RequestMapping("/result")
    public String result(Model model) {
        return "page/result_page";
    }*/

    @PostMapping("/demo/analysis")
    public String imageAnalysis(MultipartFile file, RedirectAttributes redirectAttributes, HttpServletRequest request) throws Exception {
        // 세션 받아오기
        HttpSession session = request.getSession();

        // 요청한 세션의 정보를 가져와 회원의 아이디 저장
        Member member = (Member)session.getAttribute("member");
        String memberId = member.getMemberId();
        log.info(memberId);

        // 파일 속성 출력
        log.info("imageOriginName: " + file.getOriginalFilename());
        log.info("size: " + file.getSize());
        log.info("contentType: " + file.getContentType());
        log.info("contentLength: " + file.getResource());

        Long imageId = imageService.save(memberId, file);
        log.info(""+imageId);
        Image image = imageService.findOne(imageId);

        // 세션에 이미지 객체 정보 저장
        session.setAttribute("image", image);

        // 해당 이미지명 및 경로 모델로 전송

        return "redirect:/result/"+image.getMember().getMemberId();
    }
    
    //프론트 이미지 출력
    @GetMapping(value = "/result/{memberId}")
    public String loadImage(@PathVariable String memberId, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        // 세션 받아오기
        HttpSession session = request.getSession(false);

        // 회원 및 잉미지 정보를 가져오기
        Member member = (Member) session.getAttribute("member");
        Image image = (Image) session.getAttribute("image");

        // 세션으로부터 받은 객체로 불러올 경로를 설정

        model.addAttribute("imagePath", image.getImagePath());

        return "page/result_page";
    }

    @GetMapping(value = "/imageUpload/{imagename}")
    public void LoadImage(@PathVariable("imagename") String imagename, HttpServletResponse response) {
        File image = new File("C:\\Users\\yunhc\\Desktop\\Aim_Project\\imageUpload\\" + imagename);
        int cur;
        try {
            FileInputStream fileIn = new FileInputStream(image);
            BufferedInputStream bufIn = new BufferedInputStream(fileIn);
            ServletOutputStream ostream = response.getOutputStream();
            while ((cur = bufIn.read()) != -1) {
                ostream.write(cur);
            }
            ostream.flush();
            bufIn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
