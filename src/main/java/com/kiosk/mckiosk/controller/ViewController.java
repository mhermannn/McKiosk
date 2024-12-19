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

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof org.springframework.security.core.userdetails.User) {
                // Użytkownik zalogowany lokalnie
                String username = authentication.getName();
                model.addAttribute("loggedUser", username);
            } else if (principal instanceof org.springframework.security.oauth2.core.user.DefaultOAuth2User) {
                // Użytkownik zalogowany przez OAuth2
                String username = ((org.springframework.security.oauth2.core.user.DefaultOAuth2User) principal).getAttribute("name");
                model.addAttribute("loggedUser", username != null ? username : "Użytkownik Google");
            } else {
                // Nieoczekiwany typ obiektu Principal (lub pusty)
                model.addAttribute("loggedUser", "Nie jesteś zalogowany");
            }
        } else {
            // Użytkownik niezalogowany
            model.addAttribute("loggedUser", "Nie jesteś zalogowany");
        }

        model.addAttribute("meals", kioskService.getMealModel().getAllMeals());
        return "productlist";
    }

//    @GetMapping("/ordertype")
//    public String showOrderTypePage() {
//        return "orderType";
//    }
//
//    @PostMapping("/ordertype")
//    public String handleOrderTypeSelection(@RequestParam("orderType") String orderType, HttpSession session) {
//        session.setAttribute("orderType", orderType); // Zapisanie typu zamówienia w sesji
//        return "redirect:/productlist";
//    }
//
//    @GetMapping("/productlist")
//    public String showProductPage(Model model, HttpSession session) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication != null && authentication.isAuthenticated()) {
//            Object principal = authentication.getPrincipal();
//
//            if (principal instanceof org.springframework.security.core.userdetails.User) {
//                String username = authentication.getName();
//                model.addAttribute("loggedUser", username);
//                String orderType = (String) session.getAttribute("orderType");
//                if (orderType != null) {
//                    model.addAttribute("orderType", orderType);
//                }
//            } else if (principal instanceof org.springframework.security.oauth2.core.user.DefaultOAuth2User) {
//                String username = ((org.springframework.security.oauth2.core.user.DefaultOAuth2User) principal).getAttribute("name");
//                model.addAttribute("loggedUser", username != null ? username : "Użytkownik Google");
//                String orderType = (String) session.getAttribute("orderType");
//                if (orderType != null) {
//                    model.addAttribute("orderType", orderType);
//                }
//            } else {
//                model.addAttribute("loggedUser", "Nie jesteś zalogowany");
//            }
//        } else {
//            model.addAttribute("loggedUser", "Nie jesteś zalogowany");
//        }
//        model.addAttribute("meals", kioskService.getMealModel().getAllMeals());
//        return "productList";
//    }
}
