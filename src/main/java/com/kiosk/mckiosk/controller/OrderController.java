package com.kiosk.mckiosk.controller;

import com.kiosk.mckiosk.model.Ingredient;
import com.kiosk.mckiosk.model.Order;
import com.kiosk.mckiosk.service.KioskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

public class OrderController {
    private final KioskService kioskService;
    public OrderController(KioskService kioskService) {
        this.kioskService = kioskService;
    }
    @GetMapping
    public List<Order> getAllOrders() {
        return kioskService.getOrderModel().getAllOrders();
    }

    @GetMapping("/{id}")
    public Optional<Order> getOrderById(@PathVariable int id) {
        return kioskService.getOrderModel().getOrderById(id);
    }

    @PostMapping
    public Order addOrder(@RequestBody Order order) {
        return kioskService.getOrderModel().addOrder(order);
    }

    @PutMapping("/{id}")
    public Order updateOrder(@PathVariable int id, @RequestBody Order order) {
        return kioskService.getOrderModel().updateOrder(id, order);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable int id) {
        kioskService.getOrderModel().deleteOrder(id);
    }
}
