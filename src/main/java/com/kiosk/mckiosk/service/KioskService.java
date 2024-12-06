package com.kiosk.mckiosk.service;
import com.kiosk.mckiosk.model.IngredientModel;
import com.kiosk.mckiosk.model.MealModel;
import com.kiosk.mckiosk.model.User;
import com.kiosk.mckiosk.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class KioskService {
    private final UserModel userModel;
    private final MealModel mealModel;
    private final IngredientModel ingredientModel;
    @Autowired
    public KioskService(@Qualifier("admin") User admin,@Qualifier("user") User user) {
        this.userModel = new UserModel();
        this.mealModel = new MealModel();
        this.ingredientModel = new IngredientModel();

        userModel.addUser(admin);
        userModel.addUser(user);
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

}
