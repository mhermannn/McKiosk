package com.kiosk.mckiosk.model;
import java.util.*;
public class MealModel {
    private final List<Meal> meals = new ArrayList<Meal>();
    private int currentId = 0;
    public List<Meal> getAllMeals() { return meals;}
    public Meal addMeal(Meal meal) {
        meal.setId(++currentId);
        meals.add(meal);
        return meal;
    }
    public Meal updateMeal(int id, Meal updatedMeal) {
        Optional<Meal> existingMeal = getMealById(id);
        if (existingMeal.isPresent()) {
            Meal meal = existingMeal.get();
            meal.setName(updatedMeal.getName());
            meal.setPrice(updatedMeal.getPrice());
            meal.setCategory(updatedMeal.getCategory());
            meal.setIngredients(updatedMeal.getIngredients());
            System.out.println("Updated meal: " + meal);
            return meal;
        } else {
            throw new NoSuchElementException("Meal not found with ID: " + id);
        }
    }
    public Optional<Meal> getMealById(int id) {
        return meals.stream().filter(meal -> meal.getId() == id).findFirst();
    }
    public boolean deleteMeal(int id) {
        return meals.removeIf(meal -> meal.getId() == id);
    }
}
