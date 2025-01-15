package com.kiosk.mckiosk.controller;

import com.kiosk.mckiosk.model.entity.Meal;
import com.kiosk.mckiosk.model.entity.Order;
import com.kiosk.mckiosk.model.enums.MealCategories;
import com.kiosk.mckiosk.model.enums.OrderPaymentType;
import com.kiosk.mckiosk.model.enums.OrderStatus;
import com.kiosk.mckiosk.model.enums.OrderType;
import com.kiosk.mckiosk.service.KioskService;
import com.kiosk.mckiosk.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;

import static com.kiosk.mckiosk.model.enums.OrderStatus.NEW;

@Controller
public class ViewController {

    private final KioskService kioskService;
    private final OrderService orderService;

    @Autowired
    public ViewController(KioskService kioskService, OrderService orderService) {
        this.kioskService = kioskService;
        this.orderService = orderService;
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
                currentOrder.setOrderStatus(OrderStatus.IN_PROGRESS);
                orderService.updateOrder(currentOrder.getOrderId(), currentOrder);
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
                Order savedOrder = orderService.addOrder(newOrder);
                session.setAttribute("currentOrder", savedOrder);
            }
            return "orderType";
        }
        return "redirect:/login";
    }

    @PostMapping("/ordertype")
    public String handleOrderTypeSelection(@RequestParam("orderType") String orderType, HttpSession session) {
        Order currentOrder = (Order) session.getAttribute("currentOrder");
        if (currentOrder != null) {
            currentOrder.setOrderType(OrderType.valueOf(orderType.toUpperCase()));
            orderService.updateOrder(currentOrder.getOrderId(), currentOrder);
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
                orderService.updateOrder(currentOrder.getOrderId(), currentOrder);
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
                orderService.updateOrder(currentOrder.getOrderId(), currentOrder);
            }
        });
        return "redirect:/shoppingcart";
    }

    @GetMapping("/adminPage")
    public String showAdminPage(Model model, HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            model.addAttribute("loggedUser", username);
        } else {
            model.addAttribute("loggedUser", "Nie jesteś zalogowany");
        }

        model.addAttribute("orderStats", kioskService.getOrderStatistics());
        return "adminPage";
    }
    @GetMapping("/meal/{id}/editMeal")
    public String showEditMeal(@PathVariable int id, Model model, HttpSession session) {
        kioskService.getMealModel().getMealById(id).ifPresentOrElse(
                meal -> model.addAttribute("meal", meal),
                () -> model.addAttribute("error", "Meal not found")
        );
        model.addAttribute("mealCategories", kioskService.getMealModel().getMealCategories());
        return "editMeal";
    }
    @PostMapping("/meal/{id}/editMeal")
    public String saveEditMeal(@PathVariable int id, @RequestParam String name,
                               @RequestParam String category, @RequestParam double price,
                               Model model) {
        try {
            Meal updatedMeal = new Meal();
            updatedMeal.setId(id);
            updatedMeal.setName(name);
            updatedMeal.setCategory(MealCategories.valueOf(category));
            updatedMeal.setPrice(String.valueOf(price));
            updatedMeal.setIngredients(kioskService.getMealModel().getMealById(id).get().getIngredients());
            System.out.println(updatedMeal);
            kioskService.getMealModel().updateMeal(id, updatedMeal);
            kioskService.saveMealsToCSV();
            return "redirect:/productlist";
        } catch (Exception e) {
            model.addAttribute("error", "Błąd podczas aktualizacji posiłku: " + e.getMessage());
            return "editMeal";
        }
    }

}