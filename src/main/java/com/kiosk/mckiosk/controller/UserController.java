package com.kiosk.mckiosk.controller;

import com.kiosk.mckiosk.model.entity.User;
import com.kiosk.mckiosk.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserModel userModel;

    @Autowired
    public UserController(UserModel userModel) {
        this.userModel = userModel;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userModel.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(users);
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable int id) {
        return userModel.getUserById(id)
                .<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No user with that ID: " + id));
    }

//    @PostMapping
//    public ResponseEntity<User> addUser(@RequestBody User user) {
//        try {
//            User createdUser = userModel.addUser(user);
//            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(null);
//        }
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<User> updateUser(@PathVariable int id, @RequestBody User user) {
//        try {
//            User updatedUser = userModel.updateUser(id, user);
//            return ResponseEntity.ok(updatedUser);
//        } catch (NoSuchElementException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteUser(@PathVariable int id) {
//        if (userModel.deleteUser(id)) {
//            return ResponseEntity.ok("User deleted successfully.");
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body("User with ID " + id + " does not exist.");
//        }
//    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        try {
            Optional<User> existingUser = userModel.getAllUsers().stream()
                    .filter(u -> u.getLogin().equals(user.getLogin()))
                    .findFirst();

            if (existingUser.isPresent()) {
                boolean isPasswordMatch = userModel.matchesPassword(
                        user.getPassword(), existingUser.get().getPassword());
                if (isPasswordMatch) {
                    return ResponseEntity.ok("Login successful for user: " + user.getLogin());
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body("Invalid credentials.");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the login.");
        }
    }

}
