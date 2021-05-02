package aimproject.aim.controller;

import java.io.File;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UploadForm {

    private String path="C:\\Users\\yunhc\\Downloads";

    @RequestMapping("/demo")
    public String demo(Model model)
    {
        return "page/demo_page";
    }

    @RequestMapping("/result_image")
    public String result(@RequestParam("formFileLg") MultipartFile multi,HttpServletRequest request,HttpServletResponse response, Model model)
    {
        String url = null;

        try {

            //String uploadpath = request.getServletContext().getRealPath(path);
            String uploadpath = path;
            String originFilename = multi.getOriginalFilename();
            String extName = originFilename.substring(originFilename.lastIndexOf("."),originFilename.length());
            long size = multi.getSize();
            String saveFileName = genSaveFileName(extName);

            System.out.println("uploadpath : " + uploadpath);
            System.out.println("originFilename : " + originFilename);
            System.out.println("extensionName : " + extName);
            System.out.println("size : " + size);
            System.out.println("saveFileName : " + saveFileName);

            if(!multi.isEmpty())//파일 업로드
            {
                File file = new File(uploadpath, saveFileName);
                multi.transferTo(file);

                model.addAttribute("filename", saveFileName);
                model.addAttribute("uploadPath", file.getAbsolutePath());
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
        return "redirect:demo"; //추후 결과창으로 이동, 혹은 결과와 같이 이동
    }

    // 현재 시간을 기준으로 파일 이름 생성
    private String genSaveFileName(String extName) {
        String fileName = "";

        Calendar calendar = Calendar.getInstance();
        fileName += calendar.get(Calendar.YEAR);
        fileName += calendar.get(Calendar.MONTH);
        fileName += calendar.get(Calendar.DATE);
        fileName += calendar.get(Calendar.HOUR);
        fileName += calendar.get(Calendar.MINUTE);
        fileName += calendar.get(Calendar.SECOND);
        fileName += calendar.get(Calendar.MILLISECOND);
        fileName += extName;

        return fileName;
    }
}
