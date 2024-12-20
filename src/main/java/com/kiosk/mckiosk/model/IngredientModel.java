package com.kiosk.mckiosk.model;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class IngredientModel {
    private final List<Ingredient> ingredients = new ArrayList<>();

    public Ingredient addIngredient(Ingredient ingredient) {// Auto-increment ID
        ingredients.add(ingredient);
        return ingredient;
    }

    public Ingredient getIngredientByName(String name) {
        return ingredients.stream()
                .filter(ingredient -> ingredient.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public Ingredient getIngredientById(int id) {
        return ingredients.stream()
                .filter(ingredient -> ingredient.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Ingredient not found with ID: " + id));
    }

    public List<Ingredient> getAllIngredients() {
        return new ArrayList<>(ingredients); // Return a copy to prevent modification
    }

    public Ingredient updateIngredient(int id, Ingredient updatedIngredient) {
        Ingredient existingIngredient = getIngredientById(id);
        existingIngredient.setName(updatedIngredient.getName());
        return existingIngredient;
    }

    public boolean deleteIngredient(int id) {
        return ingredients.removeIf(ingredient -> ingredient.getId() == id);
    }
}
