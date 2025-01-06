package com.kiosk.mckiosk.model;

public class ShoppingCartItem {
    private String mealName;

    public ShoppingCartItem() {}

    public ShoppingCartItem(String mealName) {
        this.mealName = mealName;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "mealName='" + mealName + '\'' +
                '}';
    }
}