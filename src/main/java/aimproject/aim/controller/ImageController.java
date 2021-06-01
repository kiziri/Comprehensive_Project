package aimproject.aim.controller;

import aimproject.aim.model.Image;
import aimproject.aim.model.Member;
import aimproject.aim.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.Utf8Encoder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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

    @PostMapping("/demo/analysis")
    public String imageAnalysis(MultipartFile file, RedirectAttributes redirectAttributes, HttpServletRequest request) throws Exception {
        // 세션 받아오기
        HttpSession session = request.getSession();

        // 요청한 세션의 정보를 가져와 회원의 아이디 저장
        Member member = (Member)session.getAttribute("member");
        String memberId = member.getMemberId();

        Long imageId = imageService.save(memberId, file);
        Image image = imageService.findOne(imageId);
        log.info(image.getImagePath());

        // 세션에 이미지 객체 정보 저장
        session.setAttribute("image", image);

        // 해당 이미지명 및 경로 모델로 전송

        return "redirect:/result/"+image.getMember().getMemberId();
    }
    
    //프론트 이미지 출력
    @GetMapping(value = "/result/{memberId}")
    public String loadImage(@PathVariable String memberId, Model model, HttpServletRequest request) {
        // 세션 받아오기
        HttpSession session = request.getSession();

        // 세션 이미지 정보 및 DB 이미지 정보 가져오기
        Image imageSession = (Image) session.getAttribute("image");
        String imagePath = imageSession.getImagePath();

        // 이미지 분석 요청
        Image image = imageService.findByImageName(memberId, imageSession.getImageName());
        JSONObject resultJSON = imageService.requestForAnalysis(image);

        //결과 변환
        JSONArray jsonArray = (JSONArray) resultJSON.get("data");

        // 세션으로부터 받은 객체로 불러올 경로를 설정
        model.addAttribute("imagePath", imagePath);
        model.addAttribute("result_json",jsonArray.toString());

        return "page/result_page";
    }


}
