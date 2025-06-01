package com.example.usermanagement;

import java.util.List;
import java.util.Optional;

public class UserApiController {
    private final UserService userService;

    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    public String createUser(String username, String email) {
        try {
            User newUser = new User(username, email);
            userService.addUser(newUser);
            return "User '" + username + "' created successfully.";
        } catch (IllegalArgumentException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String getUserDetails(String username) {
        Optional<User> user = userService.getUserByUsername(username);
        return user.map(User::toString).orElse("User not found.");
    }

    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}