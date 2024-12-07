package com.kiosk.mckiosk.controller;

import com.kiosk.mckiosk.model.Ingredient;
import com.kiosk.mckiosk.service.KioskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meals/{mealId}/ingredients")
public class MealIngredientController {
    private final KioskService kioskService;

    @Autowired
    public MealIngredientController(KioskService kioskService) {
        this.kioskService = kioskService;
    }

    @GetMapping
    public List<Ingredient> getIngredientsForMeal(@PathVariable int mealId) {
        return kioskService.getMealModel().getIngredientsByMealId(mealId);
    }

    @PostMapping("/{ingredientId}")
    public Ingredient addIngredientToMeal(@PathVariable int mealId, @PathVariable int ingredientId) {
        return kioskService.getMealModel().addIngredientToMeal(mealId, ingredientId);
    }

    @DeleteMapping("/{ingredientId}")
    public void deleteIngredientFromMeal(@PathVariable int mealId, @PathVariable int ingredientId) {
        kioskService.getMealModel().deleteIngredientFromMeal(mealId, ingredientId);
    }
}
