package aimproject.aim.controller;


import org.springframework.web.bind.annotation.RequestMapping;

@org.springframework.stereotype.Controller
public class Controller {

    @RequestMapping("home")
    public String home() {
        return "page/main_page";
    }
}
