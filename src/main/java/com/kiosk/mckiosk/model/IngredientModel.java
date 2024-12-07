package com.kiosk.mckiosk.model;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class IngredientModel {
    private final List<Ingredient> ingredients = new ArrayList<>();
    private int currentId = 0;

    // Dodanie składnika
    public Ingredient addIngredient(Ingredient ingredient) {
        ingredient.setId(++currentId); // Auto-increment ID
        ingredients.add(ingredient);
        return ingredient;
    }

    // Pobranie składnika po ID
    public Ingredient getIngredientById(int id) {
        return ingredients.stream()
                .filter(ingredient -> ingredient.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Ingredient not found with ID: " + id));
    }

    // Pobranie wszystkich składników
    public List<Ingredient> getAllIngredients() {
        return new ArrayList<>(ingredients); // Return a copy to prevent modification
    }

    // Edycja składnika
    public Ingredient updateIngredient(int id, Ingredient updatedIngredient) {
        Ingredient existingIngredient = getIngredientById(id);
        existingIngredient.setName(updatedIngredient.getName());
        return existingIngredient;
    }

    // Usunięcie składnika
    public boolean deleteIngredient(int id) {
        return ingredients.removeIf(ingredient -> ingredient.getId() == id);
    }
}
