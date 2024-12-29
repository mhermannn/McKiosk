package com.kiosk.mckiosk.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    private int customerId;

    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ElementCollection
    @CollectionTable(name = "order_shopping_cart", joinColumns = @JoinColumn(name = "order_id"))
    @Column(name = "meal_name") // Pole w tabeli `order_shopping_cart` dla każdego wpisu listy
    private List<String> shoppingCart = new ArrayList<>();

    public Order() {}

    public Order(int customerId, OrderType orderType, OrderStatus orderStatus) {
        this.customerId = customerId;
        this.orderType = orderType;
        this.orderStatus = orderStatus;
    }

    // Gettery i settery
    public int getOrderId() { return orderId; }

    public int getCustomerId() { return customerId; }

    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public OrderType getOrderType() { return orderType; }

    public void setOrderType(OrderType orderType) { this.orderType = orderType; }

    public OrderStatus getOrderStatus() { return orderStatus; }

    public void setOrderStatus(OrderStatus orderStatus) { this.orderStatus = orderStatus; }

    public List<String> getShoppingCart() { return shoppingCart; }

    public void setShoppingCart(List<String> shoppingCart) { this.shoppingCart = shoppingCart; }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", customerId=" + customerId +
                ", orderType=" + orderType +
                ", orderStatus=" + orderStatus +
                ", shoppingCart=" + shoppingCart +
                '}';
    }
}
