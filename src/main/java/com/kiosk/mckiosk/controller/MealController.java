package com.kiosk.mckiosk.controller;

import com.kiosk.mckiosk.model.entity.Meal;
import com.kiosk.mckiosk.service.KioskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/meals")
public class MealController {
    private final KioskService kioskService;

    @Autowired
    public MealController(KioskService kioskService) {
        this.kioskService = kioskService;
    }

    @GetMapping
    public List<Meal> getAllMeals() {
        return kioskService.getMealModel().getAllMeals();
    }

    @GetMapping("/{id}")
    public Optional<Meal> getMealById(@PathVariable int id) {
        return kioskService.getMealModel().getMealById(id);
    }

    @PostMapping
    public Meal addMeal(@RequestBody Meal meal) {
        return kioskService.getMealModel().addMeal(meal);
    }

    @PutMapping("/{id}")
    public Meal updateMeal(@PathVariable int id, @RequestBody Meal meal) {
        return kioskService.getMealModel().updateMeal(id, meal);
    }

    @DeleteMapping("/{id}")
    public void deleteMeal(@PathVariable int id) {
        kioskService.getMealModel().deleteMeal(id);
    }
}