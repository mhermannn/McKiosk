package com.kiosk.mckiosk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ViewController {

    @GetMapping("/")
    public String showHomePage(Model model) {
        return "welcome";
    }
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
    @GetMapping("/productlist")
    public String showProductPage() {
        return "productlist";
    }

}
