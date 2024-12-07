package com.kiosk.mckiosk.model;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class MealModel {
    private final List<Meal> meals = new ArrayList<>();
    private int currentId = 0;
    private final IngredientModel ingredientModel;

    public MealModel(IngredientModel ingredientModel) {
        this.ingredientModel = ingredientModel;
    }

    // Pobranie wszystkich posiłków
    public List<Meal> getAllMeals() {
        return meals;
    }

    // Dodanie nowego posiłku
    public Meal addMeal(Meal meal) {
        meal.setId(++currentId);
        meals.add(meal);
        return meal;
    }

    // Aktualizacja posiłku
    public Meal updateMeal(int id, Meal updatedMeal) {
        Meal existingMeal = getMealById(id).orElseThrow(() -> new NoSuchElementException("Meal not found with ID: " + id));
        existingMeal.setName(updatedMeal.getName());
        existingMeal.setPrice(updatedMeal.getPrice());
        existingMeal.setCategory(updatedMeal.getCategory());
        existingMeal.setIngredients(updatedMeal.getIngredients());
        return existingMeal;
    }

    // Pobranie posiłku po ID
    public Optional<Meal> getMealById(int id) {
        return meals.stream().filter(meal -> meal.getId() == id).findFirst();
    }

    // Usunięcie posiłku
    public boolean deleteMeal(int id) {
        return meals.removeIf(meal -> meal.getId() == id);
    }

    // Pobranie składników w posiłku
    public List<Ingredient> getIngredientsByMealId(int mealId) {
        Meal meal = getMealById(mealId).orElseThrow(() -> new NoSuchElementException("Meal not found"));
        return meal.getIngredients();
    }

    // Dodanie składnika do posiłku
    public Ingredient addIngredientToMeal(int mealId, int ingredientId) {
        Meal meal = getMealById(mealId).orElseThrow(() -> new NoSuchElementException("Meal not found"));
        Ingredient ingredient = ingredientModel.getIngredientById(ingredientId);
        meal.getIngredients().add(ingredient);
        return ingredient;
    }

    // Usunięcie składnika z posiłku
    public boolean deleteIngredientFromMeal(int mealId, int ingredientId) {
        Meal meal = getMealById(mealId).orElseThrow(() -> new NoSuchElementException("Meal not found"));
        return meal.getIngredients().removeIf(ingredient -> ingredient.getId() == ingredientId);
    }
}
