package com.kiosk.mckiosk.controller;

import com.kiosk.mckiosk.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.kiosk.mckiosk.service.KioskService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final KioskService kioskService;

    @Autowired
    public UserController(KioskService kioskService) {
        this.kioskService = kioskService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return kioskService.getUserModel().getAllUsers();
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable int id) {
        return kioskService.getUserModel().getUserById(id);
    }

    @PostMapping
    public User addUser(@RequestBody User user, PasswordEncoder passwordEncoder) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return kioskService.getUserModel().addUser(user, passwordEncoder);

    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User user) {
        return kioskService.getUserModel().updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        kioskService.getUserModel().deleteUser(id);
    }
}