package com.kiosk.mckiosk.controller;

import com.kiosk.mckiosk.model.User;
import com.kiosk.mckiosk.service.KioskService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session) {
        if ("admin".equals(username) && "admin".equals(password)) {
            session.setAttribute("user", username);
            return "redirect:/productlist";
        }
        return "login";
    }

    @GetMapping("/productlist")
    public String showProductPage(HttpSession session, Model model) {
        User loggedUser = (User) session.getAttribute("user");
        if (loggedUser != null) {
            model.addAttribute("loggedUser", loggedUser);
        } else {
            model.addAttribute("loggedUser", null);
        }
        model.addAttribute("meals", kioskService.getMealModel().getAllMeals());
        return "productlist";
    }

    @PostMapping("/logout")
    public String handleLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/welcome";
    }
}
