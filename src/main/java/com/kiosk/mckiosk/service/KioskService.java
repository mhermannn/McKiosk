package com.kiosk.mckiosk.service;

import com.kiosk.mckiosk.model.*;
import com.kiosk.mckiosk.repository.OrderRepository;
import com.kiosk.mckiosk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class KioskService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final IngredientModel ingredientModel = new IngredientModel();
    private final MealModel mealModel = new MealModel(ingredientModel);
    private final OrderModel orderModel;
    private final UserModel userModel;
    private final OrderRepository orderRepository;
    private final ShoppingCartModel shoppingCartModel;

    @Autowired
    public KioskService(UserRepository userRepository, PasswordEncoder passwordEncoder,UserModel userModel, OrderModel orderModel, ShoppingCartModel shoppingCartModel, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.orderModel = orderModel;
        this.orderRepository = orderRepository;
        this.userModel = userModel;
        this.shoppingCartModel = shoppingCartModel;
    }
    public ShoppingCartModel getShoppingCartModel() {
        return shoppingCartModel;
    }
    public UserModel getUserModel() {
        return userModel;
    }

    public OrderModel getOrderModel() {
        return orderModel;
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

    public Map<String, Map<String, Map<String, Long>>> getOrderStatistics() {
        List<Order> orders = orderModel.getAllOrders();
        LocalDateTime startOfToday = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();

        List<Order> todaysOrders = orders.stream()
                .filter(order -> order.getCreatedAt().isAfter(startOfToday))
                .toList();

        List<Order> monthlyOrders = orders.stream()
                .filter(order -> order.getCreatedAt().isAfter(startOfMonth))
                .toList();

        return Map.of(
                "today", computeStats(todaysOrders),
                "month", computeStats(monthlyOrders)
        );
    }

    private Map<String, Map<String, Long>> computeStats(List<Order> orders) {
        Map<String, Long> paymentTypes = orders.stream()
                .collect(Collectors.groupingBy(order -> order.getOrderPaymentType().name(), Collectors.counting()));

        Map<String, Long> statuses = orders.stream()
                .collect(Collectors.groupingBy(order -> order.getOrderStatus().name(), Collectors.counting()));

        Map<String, Long> orderTypes = orders.stream()
                .collect(Collectors.groupingBy(order -> order.getOrderType().name(), Collectors.counting()));

        Map<String, Map<String, Long>> stats = new HashMap<>();
        stats.put("paymentTypes", paymentTypes);
        stats.put("statuses", statuses);
        stats.put("orderTypes", orderTypes);

        return stats;
    }


}
