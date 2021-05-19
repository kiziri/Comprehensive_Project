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

        String path = "src/main/resources/static/imageupload";
        String path2 = path + "/";
        String spath = "static/imageupload/";

        log.info("originalName: " + file.getOriginalFilename());
        log.info("size: " + file.getSize());
        log.info("contentType: " + file.getContentType());

        String savedName = uploadFile(file.getOriginalFilename(), file.getBytes(), path);
        String server_Path = spath + savedName;
        log.info("server_Path: " + server_Path);
        log.info("savedName: " + savedName);

        //model.addAttribute("savedName", savedName);
        //model.addAttribute("savePath",server_Path);
        attributes.addFlashAttribute("savedName", savedName);
        attributes.addFlashAttribute("savePath", server_Path);
        return "redirect:/result";
    }

    private String uploadFile(String originalName, byte[] fileData, String path) throws Exception {
        UUID uid = UUID.randomUUID();
        String savedName = uid.toString() + "_" + originalName;

        File target = new File(path, savedName);

        // FileCopyUtils : org.springframework.util 에 있음
        FileCopyUtils.copy(fileData, target);

        return savedName;
    }

    @GetMapping(value = "/static/imageUpload/{imagename}")
    public void LoadImage(@PathVariable("imagename") String imagename, HttpServletResponse response) {
        File image = new File("src/main/resources/static/imageUpload/" + imagename);
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
