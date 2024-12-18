package com.kiosk.mckiosk.model;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserModel {
    private final List<User> users = new ArrayList<User>();
    private int currentId = 0;

    public List<User> getAllUsers(){ return users;}

    public Optional<User> getUserById(int id){
        return users.stream().filter(user -> user.getId() == id).findFirst();

    }
    public User addUser(User user, PasswordEncoder passwordEncoder) {
        user.setId(++currentId);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        users.add(user);
        System.out.println("Dodano użytkownika: " + user.getLogin() + "\n");
        System.out.println(users);
        return user;
    }

    public User updateUser(int id, User updatedUser) {
        Optional<User> existingUser = getUserById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setLogin(updatedUser.getLogin());
            user.setPassword(updatedUser.getPassword());
            System.out.println("Updated personModel: " + user);
            return user;
        } else {
            throw new NoSuchElementException("User not found with ID: " + id);
        }
    }

    public boolean deleteUser(int id) {
        return users.removeIf(user -> user.getId() == id);
    }

}
