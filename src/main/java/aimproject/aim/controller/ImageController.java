package aimproject.aim.controller;

import aimproject.aim.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;

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

    @PostMapping("/image/analysis")
    public String imageAnalysis(MultipartFile file, Model model, RedirectAttributes attributes, HttpServletRequest request) throws Exception {
        //저장 폴더 주소
        String path = "src/main/resources/static/imageupload";
        //저장 경로 및 추후 불러오는 경로 주소
        String sPath = "static/imageupload/";

        //파일 속성 출력
        log.info("originalName: " + file.getOriginalFilename());
        log.info("size: " + file.getSize());
        log.info("contentType: " + file.getContentType());

        //파일 저장 이름 및 최종 경로 설정
        String savedName = setImageNameByUUID(file.getOriginalFilename(), file.getBytes(), path);
        String server_Path = sPath + savedName;
        //해당 되는 경로및 이름 출력
        log.info("server_Path: " + server_Path);
        log.info("savedName: " + savedName);

        //해당 이미지명 및 경로 저장
        //model.addAttribute("savedName", savedName);
        //model.addAttribute("savePath",server_Path);
        attributes.addFlashAttribute("savedName", savedName);
        attributes.addFlashAttribute("savePath", server_Path);
        return "redirect:/result";
    }

    private String setImageNameByUUID(String originalName, byte[] fileData, String path) throws Exception {
        
        UUID uid = UUID.randomUUID();   // UUID 랜덤 자동 생성 부문
        String imageName = uid.toString() + "_" + originalName;

        File target = new File(path, imageName);//파일명과 경로 지정

        // FileCopyUtils : org.springframework.util 에 있음
        FileCopyUtils.copy(fileData, target);//target에 해당하는 파일 생성

        return imageName;
    }

    //프론트 이미지 출력
    @GetMapping(value = "/static/imageUpload/{imagename}")
    public void LoadImage(@PathVariable("imagename") String imagename, HttpServletResponse response) {
        //이미지 경로 지정
        File image = new File("src/main/resources/static/imageUpload/" + imagename);
        int cur;
        try {
            //이미지를 스트림으로 전송
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
