package com.kiosk.mckiosk.service;

import com.kiosk.mckiosk.model.IngredientModel;
import com.kiosk.mckiosk.model.MealModel;
import com.kiosk.mckiosk.model.User;
import com.kiosk.mckiosk.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class KioskService {
    private final UserModel userModel;
    private final MealModel mealModel;
    private final IngredientModel ingredientModel;

    @Autowired
    public KioskService(@Qualifier("admin") User admin, @Qualifier("user") User user) {
        this.ingredientModel = new IngredientModel();
        this.mealModel = new MealModel(ingredientModel);
        this.userModel = new UserModel();

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
