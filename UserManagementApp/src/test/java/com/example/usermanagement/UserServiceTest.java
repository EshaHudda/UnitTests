package com.example.usermanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserService Unit Tests")
class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    @Test
    @DisplayName("Should add a new user successfully")
    void shouldAddNewUserSuccessfully() {
        User user1 = new User("john.doe", "john@example.com");
        userService.addUser(user1);

        List<User> users = userService.getAllUsers();
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals(user1, users.get(0));
    }

    @Test
    @DisplayName("Should not add a user with duplicate username")
    void shouldNotAddUserWithDuplicateUsername() {
        User user1 = new User("jane.doe", "jane@example.com");
        userService.addUser(user1);

        User user2 = new User("jane.doe", "jane.duplicate@example.com"); // Same username
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            userService.addUser(user2);
        });
        assertTrue(thrown.getMessage().contains("already exists"));
        assertEquals(1, userService.getAllUsers().size()); // Ensure only one user was added
    }

    @Test
    @DisplayName("Should retrieve a user by username")
    void shouldRetrieveUserByUsername() {
        User user1 = new User("alice", "alice@example.com");
        userService.addUser(user1);

        Optional<User> foundUser = userService.getUserByUsername("alice");
        assertTrue(foundUser.isPresent());
        assertEquals(user1, foundUser.get());
    }

    @Test
    @DisplayName("Should return empty optional for non-existent username")
    void shouldReturnEmptyOptionalForNonExistentUsername() {
        Optional<User> foundUser = userService.getUserByUsername("nonexistent");
        assertFalse(foundUser.isPresent());
    }

    @Test
    @DisplayName("Should handle null user on add")
    void shouldHandleNullUserOnAdd() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            userService.addUser(null);
        });
        assertTrue(thrown.getMessage().contains("null or empty"));
    }

    @Test
    @DisplayName("Should handle null username on add")
    void shouldHandleNullUsernameOnAdd() {
        User user = new User(null, "test@example.com");
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            userService.addUser(user);
        });
        assertTrue(thrown.getMessage().contains("null or empty"));
    }

    @Test
    @DisplayName("Should handle empty username on add")
    void shouldHandleEmptyUsernameOnAdd() {
        User user = new User("", "test@example.com");
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            userService.addUser(user);
        });
        assertTrue(thrown.getMessage().contains("null or empty"));
    }

    @Test
    @DisplayName("Should return empty list when no users are added")
    void shouldReturnEmptyListWhenNoUsersAreAdded() {
        List<User> users = userService.getAllUsers();
        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

    @Test
    @DisplayName("Should return all added users")
    void shouldReturnAllAddedUsers() {
        User user1 = new User("user1", "user1@example.com");
        User user2 = new User("user2", "user2@example.com");
        userService.addUser(user1);
        userService.addUser(user2);

        List<User> allUsers = userService.getAllUsers();
        assertEquals(2, allUsers.size());
        assertTrue(allUsers.contains(user1));
        assertTrue(allUsers.contains(user2));
    }
}