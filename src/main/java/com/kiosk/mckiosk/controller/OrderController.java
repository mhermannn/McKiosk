package com.kiosk.mckiosk.controller;

import com.kiosk.mckiosk.model.entity.Order;
import com.kiosk.mckiosk.service.KioskService;
import com.kiosk.mckiosk.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

public class OrderController {
    private final KioskService kioskService;
    private final OrderService orderService;
    public OrderController(KioskService kioskService, OrderService orderService) {
        this.kioskService = kioskService;
        this.orderService = orderService;
    }
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Optional<Order> getOrderById(@PathVariable int id) {
        return orderService.getOrderById(id);
    }

    @PostMapping
    public Order addOrder(@RequestBody Order order) {
        return orderService.addOrder(order);
    }

    @PutMapping("/{id}")
    public Order updateOrder(@PathVariable int id, @RequestBody Order order) {
        System.out.println("id: " + id + " order: " + order + "in OrderController");
        return orderService.updateOrder(id, order);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable int id) {
        orderService.deleteOrder(id);
    }
}