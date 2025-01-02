package com.kiosk.mckiosk.controller;

import com.kiosk.mckiosk.model.Order;
import com.kiosk.mckiosk.model.ShoppingCartModel;
import com.kiosk.mckiosk.service.KioskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import java.util.List;

@Controller
@RequestMapping("/shoppingcart")
public class ShoppingCartController {

    private KioskService kioskService;

    @Autowired
    public ShoppingCartController(KioskService kioskService) {
        this.kioskService = kioskService;
    }


//    @GetMapping
//    public String showCart(HttpSession session, Model model) {
//        Order currentOrder = (Order) session.getAttribute("currentOrder");
//        if (currentOrder == null) {
//            model.addAttribute("shoppingCart", List.of());
//            return "shoppingCart";
//        }
//
//        model.addAttribute("shoppingCart", shoppingCartModel.getAllItems(currentOrder));
//        return "shoppingCart";
//    }

    @PostMapping("/add")
    public String addToCart(@RequestParam String mealName, HttpSession session) {
        Order currentOrder = (Order) session.getAttribute("currentOrder");
        if (currentOrder != null) {
            kioskService.getShoppingCartModel().addToCart(currentOrder, mealName);
        }
        return "redirect:/shoppingcart";
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam String mealName, HttpSession session) {
        Order currentOrder = (Order) session.getAttribute("currentOrder");
        if (currentOrder != null) {
            kioskService.getShoppingCartModel().removeFromCart(currentOrder, mealName);
            currentOrder.getShoppingCart().remove(mealName);
            kioskService.getOrderModel().updateOrder(currentOrder.getOrderId(), currentOrder);
        }
        return "redirect:/shoppingcart";
    }

    @PostMapping("/clear")
    public String clearCart(HttpSession session) {
        Order currentOrder = (Order) session.getAttribute("currentOrder");
        if (currentOrder != null) {
            kioskService.getShoppingCartModel().clearCart(currentOrder);
            currentOrder.getShoppingCart().clear();
            kioskService.getOrderModel().updateOrder(currentOrder.getOrderId(), currentOrder);
        }
        return "redirect:/shoppingcart";
    }
}
