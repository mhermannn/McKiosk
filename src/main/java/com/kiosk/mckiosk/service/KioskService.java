package com.kiosk.mckiosk.service;

import com.kiosk.mckiosk.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class KioskService {
    private final UserModel userModel;
    private final MealModel mealModel;
    private final IngredientModel ingredientModel;

    @Autowired
    public KioskService(UserModel userModel, @Qualifier("admin") User admin, PasswordEncoder passwordEncoder) {
        this.ingredientModel = new IngredientModel();
        this.mealModel = new MealModel(ingredientModel);
        this.userModel = userModel;

        userModel.addUser(admin, passwordEncoder);

//        System.out.println("\nWstrzyknięty użytkownik: " + admin);
//        System.out.println("Current users: KioskService dodanie " + userModel.getAllUsers());
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public MealModel getMealModel() {
        return mealModel;
    }

    public IngredientModel getIngredientModel() {
        return ingredientModel;
    }

    public void loadMealsFromCSV(String filePath) {
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                int id = Integer.parseInt(data[0]);
                String name = data[1];
                MealCategories category = MealCategories.valueOf(data[2]);
                String price = data[3];
                String[] ingredientNames = data[4].replaceAll("\"", "").split(",");
                List<Ingredient> ingredients = new ArrayList<>();
                for (String ingredientName : ingredientNames) {
                    Ingredient ingredient = ingredientModel.getIngredientByName(ingredientName.trim());
                    if (ingredient != null) {
                        ingredients.add(ingredient);
                    }
                }
                Meal meal = new Meal(id, name, category, price, ingredients);
                mealModel.addMeal(meal);
            }
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void loadIngredientsFromCSV(String filePath) {
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                int id = Integer.parseInt(data[0]);
                String name = data[1];
                Ingredient ingredient = new Ingredient(id, name);
                ingredientModel.addIngredient(ingredient);
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }
}
