package com.rslhdyt.google.drive.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping(value = {"/"})
    public String index() {
        return "index.html";
    }

    @GetMapping(value = {"/google-drive"})
    public String googleDrive() {
        return "dashboard.html";
    }
}
