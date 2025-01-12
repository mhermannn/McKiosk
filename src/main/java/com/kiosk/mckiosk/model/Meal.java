package com.kiosk.mckiosk.model;

import java.util.*;

public class Meal {
    private int id;
    private String name;
    private MealCategories category;
    private String price;
    private List<Ingredient> ingredients;
    public Meal(int id, String name, MealCategories category, String price, List<Ingredient> ingredients) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.ingredients = ingredients;
    }
    public Meal(){}
    @Override
    public String toString() {
        return id+ ". " +name + ", " + category + ", " + price;
    }
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public MealCategories getCategory() {return category;}
    public void setCategory(MealCategories category) {this.category = category;}
    public String getPrice() {return price;}
    public void setPrice(String price) {this.price = price;}
    public List<Ingredient> getIngredients() {return ingredients;}
    public void setIngredients(List<Ingredient> ingredients) {this.ingredients = ingredients;}
}
