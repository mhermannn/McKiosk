package com.kiosk.mckiosk.controller;

import com.kiosk.mckiosk.model.entity.Ingredient;
import com.kiosk.mckiosk.service.KioskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {
    private final KioskService kioskService;

    @Autowired
    public IngredientController(KioskService kioskService) {
        this.kioskService = kioskService;
    }

    @GetMapping
    public List<Ingredient> getAllIngredients() {
        return kioskService.getIngredientModel().getAllIngredients();
    }

    @GetMapping("/{id}")
    public Ingredient getIngredientById(@PathVariable int id) {
        return kioskService.getIngredientModel().getIngredientById(id);
    }

    @PostMapping
    public Ingredient addIngredient(@RequestBody Ingredient ingredient) {
        return kioskService.getIngredientModel().addIngredient(ingredient);
    }

    @PutMapping("/{id}")
    public Ingredient updateIngredient(@PathVariable int id, @RequestBody Ingredient ingredient) {
        return kioskService.getIngredientModel().updateIngredient(id, ingredient);
    }

    @DeleteMapping("/{id}")
    public void deleteIngredient(@PathVariable int id) {
        kioskService.getIngredientModel().deleteIngredient(id);
    }
}