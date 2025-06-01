package com.example.usermanagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService {
    private final List<User> users;

    public UserService() {
        this.users = new ArrayList<>();
    }

    public void addUser(User user) {
        if (user == null || user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("User and username cannot be null or empty.");
        }
        // Simple check for uniqueness
        if (users.stream().anyMatch(u -> u.getUsername().equals(user.getUsername()))) {
            throw new IllegalArgumentException("User with this username already exists.");
        }
        this.users.add(user);
    }

    public Optional<User> getUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return Optional.empty();
        }
        return users.stream()
                    .filter(user -> user.getUsername().equals(username))
                    .findFirst();
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users); // Return a copy to prevent external modification
    }
}