package com.kiosk.mckiosk.model;

import com.kiosk.mckiosk.model.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartModel {

    // Dodanie elementu do koszyka
    public void addToCart(Order order, String mealName) {
        List<String> shoppingCart = order.getShoppingCart();
        shoppingCart.add(mealName);
        order.setShoppingCart(shoppingCart);
    }

    // Usunięcie elementu z koszyka
    public void removeFromCart(Order order, String mealName) {
        List<String> shoppingCart = order.getShoppingCart();
        shoppingCart.remove(mealName);
        order.setShoppingCart(shoppingCart);
    }

    // Pobranie wszystkich elementów koszyka
    public List<String> getAllItems(Order order) {
        return order.getShoppingCart();
    }

    // Wyczyść koszyk
    public void clearCart(Order order) {
        order.getShoppingCart().clear();
    }
}
