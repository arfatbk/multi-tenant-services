package com.arfat.OAuth_server.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping
    @ResponseBody
    public String home() {
        return "home";
    }

    @GetMapping("/home")
    @ResponseBody
    public String home1() {
        return "home";
    }
}
