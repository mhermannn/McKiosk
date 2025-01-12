package com.kiosk.mckiosk.controller;

import com.kiosk.mckiosk.model.*;
import com.kiosk.mckiosk.service.KioskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;

import static com.kiosk.mckiosk.model.OrderStatus.NEW;

@Controller
@RequestMapping("/shoppingcart")
public class ShoppingCartController {

    private KioskService kioskService;

    @Autowired
    public ShoppingCartController(KioskService kioskService) {
        this.kioskService = kioskService;
    }

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

    @PostMapping("/checkout")
    public String checkoutCart(HttpSession session, Model model) {
        Order currentOrder = (Order) session.getAttribute("currentOrder");
        if (currentOrder != null) {
            model.addAttribute("shoppingCart", currentOrder.getShoppingCart());
            double totalPrice = currentOrder.getShoppingCart()
                    .stream()
                    .mapToDouble(mealName -> kioskService.getMealModel().getPriceByName(mealName))
                    .sum();
            model.addAttribute("totalPrice", totalPrice);
            session.setAttribute("totalPrice", totalPrice);
        }
        return "checkoutCart";
    }
    @GetMapping("/insufficient-funds")
    public String showInsufficientFundsPage(Model model) {
        return "insufficientFunds";
    }


    @PostMapping("/checkout/confirm")
    public String confirmPayment(@RequestParam("paymentMethod") String paymentMethod, HttpSession session, Model model) {
        Order currentOrder = (Order) session.getAttribute("currentOrder");
        Double totalPrice = (Double) session.getAttribute("totalPrice");
        if (totalPrice == null) {
            model.addAttribute("error", "Nie można znaleźć ceny całkowitej.");
            return "shoppingCart";
        }

        if (currentOrder != null) {
            currentOrder.setOrderPaymentType(OrderPaymentType.valueOf(paymentMethod.toUpperCase()));

            int customerId = currentOrder.getCustomerId();
            User currentUser = kioskService.getUserModel().getAllUsers().stream()
                    .filter(u -> u.getId() == customerId)
                    .findFirst()
                    .orElse(null);

            if (currentUser != null) {
                if (currentUser.getResources() >= totalPrice) {
                    currentUser.setResources(currentUser.getResources() - totalPrice);
                    kioskService.getUserModel().updateUser(currentUser.getId(), currentUser);

                    currentOrder.setOrderStatus(OrderStatus.COMPLETED);
                    kioskService.getOrderModel().updateOrder(currentOrder.getOrderId(), currentOrder);

                    Order newOrder = new Order();
                    newOrder.setCustomerId(currentUser.getId());
                    newOrder.setOrderStatus(NEW);
                    newOrder.setOrderType(currentOrder.getOrderType());
                    newOrder.setOrderPaymentType(OrderPaymentType.UNKNOWN);
                    Order savedOrder = kioskService.getOrderModel().addOrder(newOrder);
                    session.setAttribute("currentOrder", savedOrder);

                    model.addAttribute("message", "Zamówienie nr " + currentOrder.getOrderId() + " prawidłowo opłacone.");
                    return "welcome";
                } else {
                    currentOrder.setOrderStatus(OrderStatus.CANCELLED);
                    kioskService.getOrderModel().updateOrder(currentOrder.getOrderId(), currentOrder);

                    Order newOrder = new Order();
                    newOrder.setCustomerId(currentUser.getId());
                    newOrder.setOrderStatus(NEW);
                    newOrder.setOrderType(currentOrder.getOrderType());
                    newOrder.setOrderPaymentType(OrderPaymentType.UNKNOWN);

                    newOrder.setShoppingCart(new ArrayList<>(currentOrder.getShoppingCart()));

                    Order savedOrder = kioskService.getOrderModel().addOrder(newOrder);
                    session.setAttribute("currentOrder", savedOrder);

                    model.addAttribute("error", "Za mało środków. Utworzono nowe zamówienie zawierające pozycje z poprzedniego koszyka.");
                    return "insufficientFunds";
                }
            } else {
                model.addAttribute("error", "Nie znaleziono użytkownika.");
                return "redirect:/";
            }
        }
        System.out.println("redirect:/");
        return "redirect:/";
    }
}