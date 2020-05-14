package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String homepage(){ //will be probably changed and renamed
        return "index"; //just for initial testing purpose
    }


}
