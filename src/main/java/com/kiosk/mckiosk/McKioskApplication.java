package com.kiosk.mckiosk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import com.kiosk.mckiosk.service.KioskService;

@SpringBootApplication
@ImportResource("classpath:beans.xml")
public class McKioskApplication implements CommandLineRunner {
    @Autowired
    private KioskService kioskService;

    public static void main(String[] args) {
        SpringApplication.run(McKioskApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\nPosiłki z pliku CSV:");
        kioskService.loadMealsFromCSV("src/main/resources/MEALS.csv");
        kioskService.getMealModel().getAllMeals().forEach(System.out::println);

        System.out.println("\nSkładniki z pliku CSV:");
        kioskService.loadIngredientsFromCSV("src/main/resources/INGREDIENTS.csv");
        kioskService.getIngredientModel().getAllIngredients().forEach(System.out::println);

    }
}
