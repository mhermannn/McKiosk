package com.kiosk.mckiosk.model;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartModel {

    public ShoppingCartModel() {
    }

    public void addToCart(Order order, String mealName) {
        List<String> shoppingCart = order.getShoppingCart();
        shoppingCart.add(mealName);
        order.setShoppingCart(shoppingCart);
    }

    public void removeFromCart(Order order, String mealName) {
        List<String> shoppingCart = order.getShoppingCart();
        shoppingCart.remove(mealName);
        order.setShoppingCart(shoppingCart);
    }

    public List<String> getAllItems(Order order) {
        return order.getShoppingCart();
    }

    public void clearCart(Order order) {
        order.getShoppingCart().clear();
    }
}