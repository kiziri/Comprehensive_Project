package aimproject.aim.controller;

import java.io.File;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ImageController {

    private String path="C:\\Users\\yunhc\\Downloads";
    private String path2="C:\\Users\\yunhc\\Downloads\\";
    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);


    @RequestMapping("/demo")
    public String demo(Model model)
    {
        return "page/demo_page";
    }

    @RequestMapping("/result")
    public String result(Model model)
    {
        return "page/result_page";
    }

    @RequestMapping(value = "/imageAnalysis", method = RequestMethod.POST)
    public String uploadForm(MultipartFile file, Model model, RedirectAttributes attributes) throws Exception {

        logger.info("originalName: " + file.getOriginalFilename());
        logger.info("size: " + file.getSize());
        logger.info("contentType: " + file.getContentType());

        String savedName = uploadFile(file.getOriginalFilename(), file.getBytes());
        String server_Path= path2+savedName;
        logger.info("server_Path: "+ server_Path);
        logger.info("savedName: "+ savedName);

        //model.addAttribute("savedName", savedName);
        //model.addAttribute("savePath",server_Path);
        attributes.addFlashAttribute("savedName", savedName);
        attributes.addFlashAttribute("savePath",server_Path);
        return "redirect:/result";
    }

    private String uploadFile(String originalName, byte[] fileData) throws Exception {
        UUID uid = UUID.randomUUID();
        String savedName = uid.toString() + "_" + originalName;

        File target = new File(path, savedName);

        // FileCopyUtils : org.springframework.util 에 있음
        FileCopyUtils.copy(fileData, target);

        return savedName;
    }
}
