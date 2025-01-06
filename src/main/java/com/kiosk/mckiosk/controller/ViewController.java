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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;

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

    @GetMapping("/meal/{id}")
    public String showMealDetails(@PathVariable int id, Model model) {
        kioskService.getMealModel().getMealById(id).ifPresentOrElse(
                meal -> model.addAttribute("meal", meal),
                () -> model.addAttribute("error", "Meal not found")
        );
        return "mealPage";
    }

    @PostMapping("/changeOrderType")
    public String handleOrderTypeSelection(HttpSession session, RedirectAttributes redirectAttributes) {
        Order currentOrder = (Order) session.getAttribute("currentOrder");

        if (currentOrder != null) {
            OrderType currentOrderType = currentOrder.getOrderType();
            if (currentOrderType != null) {
                if (currentOrderType.equals(OrderType.TO_GO)) {
                    currentOrder.setOrderType(OrderType.ON_SITE);
                } else if (currentOrderType.equals(OrderType.ON_SITE)) {
                    currentOrder.setOrderType(OrderType.TO_GO);
                }
                kioskService.getOrderModel().updateOrder(currentOrder.getOrderId(), currentOrder);
                redirectAttributes.addFlashAttribute("message", "Typ zamówienia zmieniony na: " + currentOrder.getOrderType());
            } else {
                redirectAttributes.addFlashAttribute("error", "Typ zamówienia jest nieprawidłowy.");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Nie znaleziono aktywnego zamówienia.");
        }

        return "redirect:/productlist";
    }


    @GetMapping("/ordertype")
    public String showOrderTypePage(HttpSession session, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            if (session.getAttribute("currentOrder") == null) {
                Order newOrder = new Order();
                newOrder.setCustomerId(kioskService.getUserModel().getUserIdByUsername(username));
                newOrder.setOrderStatus(NEW);
                newOrder.setOrderType(OrderType.UNKNOWN);
                newOrder.setOrderPaymentType(OrderPaymentType.UNKNOWN);
                Order savedOrder = kioskService.getOrderModel().addOrder(newOrder);
                session.setAttribute("currentOrder", savedOrder);
            }
            return "orderType";
        }
        return "redirect:/login";
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
            System.out.println(currentOrder.getShoppingCart());
            int cartSize = currentOrder.getShoppingCart().size();
            model.addAttribute("cartSize", cartSize);
        } else {
            model.addAttribute("orderType", null);
        }
        model.addAttribute("meals", kioskService.getMealModel().getAllMeals());
        return "productList";
    }

    @GetMapping("/shoppingcart")
    public String showShoppingCart(HttpSession session, Model model) {
        Order currentOrder = (Order) session.getAttribute("currentOrder");
        if (currentOrder != null) {
            model.addAttribute("shoppingCart", currentOrder.getShoppingCart());
        } else {
            System.out.println("Cant get shopping cart in viewController getmapping");
            model.addAttribute("shoppingCart", new ArrayList<>());
        }
        return "shoppingCart";
    }

    @PostMapping("/productlist/add")
    public String addProductToCart(@RequestParam("mealId") int mealId, HttpSession session) {
        Order currentOrder = (Order) session.getAttribute("currentOrder");
        System.out.println(kioskService.getMealModel().getMealById(mealId));
        kioskService.getMealModel().getMealById(mealId).ifPresent(meal -> {
            if (currentOrder != null) {
                System.out.println(currentOrder.getOrderId());
                currentOrder.getShoppingCart().add(meal.getName());
                currentOrder.setOrderStatus(OrderStatus.IN_PROGRESS);
                kioskService.getOrderModel().updateOrder(currentOrder.getOrderId(), currentOrder);
            }
            else {
                System.out.println("currentOrder is null in addProductToCart ViewController");
            }
        });
        return "redirect:/productlist";
    }

    @PostMapping("/meal/{id}/add")
    public String addMealToCart(@PathVariable int id, HttpSession session) {
        Order currentOrder = (Order) session.getAttribute("currentOrder");
        kioskService.getMealModel().getMealById(id).ifPresent(meal -> {
            if (currentOrder != null) {
                currentOrder.getShoppingCart().add(meal.getName());
                currentOrder.setOrderStatus(OrderStatus.IN_PROGRESS);
                kioskService.getOrderModel().updateOrder(currentOrder.getOrderId(), currentOrder);
            }
        });
        return "redirect:/shoppingcart";
    }
}