package com.kiosk.mckiosk.model;

import com.kiosk.mckiosk.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderModel {
    private final OrderRepository orderRepository;

    public OrderModel(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(int id) {
        return orderRepository.findById(id);
    }

    public Order addOrder(Order order) {
        return orderRepository.save(order);
    }

    public Order updateOrder(int id, Order updatedOrder) {
        System.out.println("Reciving order in OrderModel with id " + id + " to " + updatedOrder);
        return orderRepository.findById(id)
                .map(existingOrder -> {
                    existingOrder.setCustomerId(updatedOrder.getCustomerId());
                    existingOrder.setOrderType(updatedOrder.getOrderType());
                    existingOrder.setOrderStatus(updatedOrder.getOrderStatus());
                    existingOrder.setShoppingCart(updatedOrder.getShoppingCart());
                    return orderRepository.save(existingOrder);
                })
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + id));
    }

    public void deleteOrder(int id) {
        orderRepository.deleteById(id);
    }
}
