package com.kiosk.mckiosk.controller;

import com.kiosk.mckiosk.model.entity.User;
import com.kiosk.mckiosk.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public List<User> getAllUsers() {
        return userModel.getAllUsers();
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable int id) {
        return userModel.getUserById(id);
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        return userModel.addUser(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User user) {
        return userModel.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        if (!userModel.deleteUser(id)) {
            throw new IllegalArgumentException("User with ID " + id + " does not exist.");
        }
    }
}