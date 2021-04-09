package aimproject.aim.controller;


import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@org.springframework.stereotype.Controller
public class Controller {

    @RequestMapping("/")
    public String home(Model model) {
        return "page/main_page";
    }

    @RequestMapping("/login")
    public String login(Model model) {
        return "page/login_page";
    }

}
