package com.kiosk.mckiosk.controller;

import com.kiosk.mckiosk.model.*;
import com.kiosk.mckiosk.service.KioskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import static com.kiosk.mckiosk.model.OrderStatus.NEW;

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
//            } else if (principal instanceof org.springframework.security.oauth2.core.user.DefaultOAuth2User) {
//                String username = ((org.springframework.security.oauth2.core.user.DefaultOAuth2User) principal).getAttribute("name");
//                model.addAttribute("loggedUser", username != null ? username : "Użytkownik Google");
//            } else {
//                model.addAttribute("loggedUser", "Nie jesteś zalogowany");
//            }
//        } else {
//            model.addAttribute("loggedUser", "Nie jesteś zalogowany");
//        }
//
//        //aspekt order + dodalam session u gory
//        Order currentOrder = (Order) session.getAttribute("currentOrder");
//        if (currentOrder != null) {
//            model.addAttribute("orderType", currentOrder.getOrderType());
//        } else {
//            model.addAttribute("orderType", null);
//        }
//        //
//
//        model.addAttribute("meals", kioskService.getMealModel().getAllMeals());
//        return "productList";
//    }


    @GetMapping("/meal/{id}")
    public String showMealDetails(@PathVariable int id, Model model) {
        kioskService.getMealModel().getMealById(id).ifPresentOrElse(
                meal -> model.addAttribute("meal", meal),
                () -> model.addAttribute("error", "Meal not found")
        );
        return "mealPage";
    }

    @PostMapping("/ordertype")
    public String handleOrderTypeSelection(@RequestParam("orderType") String orderType, HttpSession session) {
        // Pobierz zamówienie z sesji
        Order currentOrder = (Order) session.getAttribute("currentOrder");
        if (currentOrder != null) {
            // Ustaw typ zamówienia
            currentOrder.setOrderType(OrderType.valueOf(orderType.toUpperCase()));
            kioskService.getOrderModel().updateOrder(currentOrder.getOrderId(), currentOrder);
        }
        return "redirect:/productlist";
    }

    @GetMapping("/ordertype")
    public String showOrderTypePage(HttpSession session, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            // Utwórz nowe zamówienie, jeśli nie istnieje w sesji
            if (session.getAttribute("currentOrder") == null) {
                Order newOrder = new Order();
                newOrder.setCustomerId(kioskService.getUserModel().getUserIdByUsername(username));
                newOrder.setOrderStatus(NEW);
                newOrder.setOrderType(OrderType.UNKNOWN);
                Order savedOrder = kioskService.getOrderModel().addOrder(newOrder);
                session.setAttribute("currentOrder", savedOrder);
            }
            return "orderType";
        }
        return "redirect:/login";
    }

    @GetMapping("/productlist")
    public String showProductPage(Model model, HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof org.springframework.security.core.userdetails.User) {
                String username = authentication.getName();
                model.addAttribute("loggedUser", username);
            } else if (principal instanceof org.springframework.security.oauth2.core.user.DefaultOAuth2User) {
                String email = ((org.springframework.security.oauth2.core.user.DefaultOAuth2User) principal).getAttribute("email");
                model.addAttribute("loggedUser", email != null ? email : "Użytkownik Google");
            } else {
                model.addAttribute("loggedUser", "Nie jesteś zalogowany");
            }
        } else {
            model.addAttribute("loggedUser", "Nie jesteś zalogowany");
        }

        Order currentOrder = (Order) session.getAttribute("currentOrder");
        if (currentOrder != null) {
            model.addAttribute("orderType", currentOrder.getOrderType());
        } else {
            model.addAttribute("orderType", null);
        }

        model.addAttribute("meals", kioskService.getMealModel().getAllMeals());
        return "productList";
    }
}