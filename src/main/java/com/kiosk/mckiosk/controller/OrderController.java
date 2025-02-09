package com.kiosk.mckiosk.controller;

import com.kiosk.mckiosk.model.UserModel;
import com.kiosk.mckiosk.model.entity.Order;
import com.kiosk.mckiosk.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserModel userModel;

    public OrderController(OrderService orderService, UserModel userModel) {
        this.orderService = orderService;
        this.userModel = userModel;
    }
    
    @GetMapping
    public ResponseEntity<?> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        if (orders.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("No orders available.");
        }
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable int id) {
        Optional<Order> order = orderService.getOrderById(id);
        if (order.isPresent()) {
            return ResponseEntity.ok(order.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Order with ID " + id + " not found.");
        }
    }

    @PostMapping
    public ResponseEntity<?> addOrder(@RequestBody Order order) {
        // Walidacja czy użytkownik istnieje
        if (!userModel.getUserById(order.getCustomerId()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Customer with ID " + order.getCustomerId() + " does not exist.");
        }

        // Walidacja pól
        if (order.getOrderType() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OrderType is required and must be valid.");
        }
        if (order.getOrderStatus() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OrderStatus is required and must be valid.");
        }
        if (order.getOrderPaymentType() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OrderPaymentType is required and must be valid.");
        }

        Order newOrder = orderService.addOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Order created successfully with ID " + newOrder.getOrderId());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable int id, @RequestBody Order order) {
        Optional<Order> existingOrder = orderService.getOrderById(id);
        if (existingOrder.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Order with ID " + id + " not found for update.");
        }

        // Walidacja czy użytkownik istnieje
        if (!userModel.getUserById(order.getCustomerId()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Customer with ID " + order.getCustomerId() + " does not exist.");
        }

        // Walidacja pól
        if (order.getOrderType() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OrderType is required and must be valid.");
        }
        if (order.getOrderStatus() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OrderStatus is required and must be valid.");
        }
        if (order.getOrderPaymentType() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OrderPaymentType is required and must be valid.");
        }

        Order updatedOrder = orderService.updateOrder(id, order);
        return ResponseEntity.ok("Order with ID " + updatedOrder.getOrderId() + " updated successfully.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable int id) {
        Optional<Order> order = orderService.getOrderById(id);
        if (order.isPresent()) {
            orderService.deleteOrder(id);
            return ResponseEntity.ok("Order with ID " + id + " deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Order with ID " + id + " not found.");
        }
    }
}


