package com.kiosk.mckiosk.controller;

import com.kiosk.mckiosk.model.User;
import com.kiosk.mckiosk.service.KioskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class ViewController {

    private final KioskService kioskService;

    @Autowired
    public ViewController(KioskService kioskService) {
        this.kioskService = kioskService;
    }

    @GetMapping("/")
    public String showHomePage() {
        return "welcome";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/productlist")
    public String showProductPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User loggedUser = (User) authentication.getPrincipal();
            model.addAttribute("loggedUser", loggedUser.getLogin());
        } else {
            model.addAttribute("loggedUser", null);
        }

//        model.addAttribute("meals", kioskService.getMealModel().getAllMeals());
        return "productlist";
    }


    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
