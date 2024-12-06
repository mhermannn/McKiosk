package com.kiosk.mckiosk.controller;

import com.kiosk.mckiosk.model.Ingredient;
import com.kiosk.mckiosk.service.KioskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/meals/{mealId}/ingredients")
public class IngredientController {

    private final KioskService kioskService;

    @Autowired
    public IngredientsController(KioskService kioskService) {
        this.kioskService = kioskService;
    }

    @GetMapping
    public List<Ingredient> getIngredients(@PathVariable int mealId) {
        return kioskService.getIngredientModel().getAllIngredients();
    }
    public List<Ingredient> getIngredientsForMeal(@PathVariable int mealId) {
        return kioskService.getIngredientModel().getAllIngredientsForMeal();
    }

    @PostMapping
    public Ingredient addIngredient(@PathVariable int mealId, @RequestBody Ingredient ingredient) {
        return kioskService.getIngredientModel().addIngredientToMeal(mealId, ingredient);
    }

    @PutMapping("/{ingredientId}")
    public Ingredient updateIngredient(
            @PathVariable int mealId,
            @PathVariable int ingredientId,
            @RequestBody Ingredient updatedIngredient) {
        return kioskService.getIngredientModel().updateIngredientInMeal(mealId, ingredientId, updatedIngredient);
    }

    @DeleteMapping("/{ingredientId}")
    public void deleteIngredient(@PathVariable int mealId, @PathVariable int ingredientId) {
        kioskService.getIngredientModel().deleteIngredientFromMeal(mealId, ingredientId);
    }
}
