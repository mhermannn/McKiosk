package com.kiosk.mckiosk.model;

import com.kiosk.mckiosk.service.KioskService;

import java.util.*;

public class IngredientModel {
    private final List<Ingredient> ingredients = new ArrayList<>();
    private int currentId = 0;


    public Ingredient addIngredient(Ingredient ingredient) {
        ingredient.setId(++currentId); // Auto-increment ID
        ingredients.add(ingredient);
        return ingredient;
    }
    public Ingredient getIngredientById(int id) {
        return ingredients.stream()
                .filter(ingredient -> ingredient.getId() == id)
                .findFirst()
                .orElse(null);
    }
    public List<Ingredient> getAllIngredients() {
        return new ArrayList<>(ingredients); // Return a copy to prevent modification
    }
    public Ingredient updateIngredient(int id, Ingredient updatedIngredient) {
        Ingredient existingIngredient = getIngredientById(id);
        if (existingIngredient != null) {
            existingIngredient.setName(updatedIngredient.getName());
            System.out.println("Updated ingredient: " + existingIngredient);
            return existingIngredient;
        } else {
            throw new NoSuchElementException("Ingredient not found with ID: " + id);
        }
    }
    public boolean deleteIngredientFromMeal(int id) {
        return ingredients.removeIf(ingredient -> ingredient.getId() == id);
    }
//    BY INGREDIENTID I MEAN THE OBJECTIVE ID NOT THE ONE IT COULD BE GIVEN AS AN ELEMENT OF A MEAL
//    this file is supposed to have actual code not paths

    public Ingredient addIngredientToMeal(int mealID, int ingredientID) {
//        return addIngredientToMeal(mealID, ingredientID);

    }
    public Ingredient deleteIngredientFromMeal(int mealID, int ingredientID) {
//        return deleteIngredientFromMeal(mealID, ingredientID);

    }
    public Ingredient editIngredientToMeal(int mealID, int ingredientID) {

    }
}
