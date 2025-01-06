package com.kiosk.mckiosk.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptPasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // Przykładowe hasła
        String password1 = "admin";
        String password2 = "user";

        // Generowanie zaszyfrowanych haseł
        String hashedPassword1 = passwordEncoder.encode(password1);
        String hashedPassword2 = passwordEncoder.encode(password2);

        // Wyświetlanie zaszyfrowanych haseł
        System.out.println("Hasło admina w bcrypt: " + hashedPassword1);
        System.out.println("Hasło użytkownika w bcrypt: " + hashedPassword2);
    }
}