package aimproject.aim.controller;

import java.io.*;
import java.util.UUID;

import org.apache.coyote.Response;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ImageController {


    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);


    @RequestMapping("/demo")
    public String demo(Model model) {
        return "page/demo_page";
    }

    @RequestMapping("/result")
    public String result(Model model) {
        return "page/result_page";
    }

    @RequestMapping(value = "/imageAnalysis", method = RequestMethod.POST)
    public String uploadForm(MultipartFile file, Model model, RedirectAttributes attributes, HttpServletRequest request) throws Exception {

        String path = "src/main/resources/static/imageUpload";
        String path2 = path + "/";
        String spath = "static/imageUpload/";

        logger.info("originalName: " + file.getOriginalFilename());
        logger.info("size: " + file.getSize());
        logger.info("contentType: " + file.getContentType());

        String savedName = uploadFile(file.getOriginalFilename(), file.getBytes(), path);
        String server_Path = spath + savedName;
        logger.info("server_Path: " + server_Path);
        logger.info("savedName: " + savedName);

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
