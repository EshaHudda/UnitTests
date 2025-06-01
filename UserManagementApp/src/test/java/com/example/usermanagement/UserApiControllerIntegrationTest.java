package com.example.usermanagement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserApiController Integration Tests")
class UserApiControllerIntegrationTest {

    private UserService userService;
    private UserApiController userApiController;

    @BeforeEach
    void setUp() {
        userService = new UserService(); // Real dependency
        userApiController = new UserApiController(userService);
    }

    @Test
    @DisplayName("Should successfully create a user via API and retrieve it")
    void shouldSuccessfullyCreateAndRetrieveUser() {
        String createMessage = userApiController.createUser("apiUser1", "apiUser1@example.com");
        assertTrue(createMessage.contains("User 'apiUser1' created successfully."));

        String userDetails = userApiController.getUserDetails("apiUser1");
        assertTrue(userDetails.contains("apiUser1"));
        assertTrue(userDetails.contains("apiUser1@example.com"));

        List<User> allUsers = userApiController.getAllUsers();
        assertEquals(1, allUsers.size());
        assertEquals("apiUser1", allUsers.get(0).getUsername());
    }

    @Test
    @DisplayName("Should return error when creating duplicate user via API")
    void shouldReturnErrorOnDuplicateUserCreation() {
        userApiController.createUser("duplicateApiUser", "dup@example.com");
        String createMessage = userApiController.createUser("duplicateApiUser", "anotherdup@example.com");
        assertTrue(createMessage.contains("Error: User with this username already exists."));
        assertEquals(1, userService.getAllUsers().size()); // Only one user should exist
    }

    @Test
    @DisplayName("Should return 'User not found.' for non-existent user details via API")
    void shouldReturnNotFoundForNonExistentUser() {
        String userDetails = userApiController.getUserDetails("nonExistentApiUser");
        assertEquals("User not found.", userDetails);
    }

    @Test
    @DisplayName("Should return empty list when no users are created via API")
    void shouldReturnEmptyListWhenNoUsersAreCreated() {
        List<User> users = userApiController.getAllUsers();
        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

    @Test
    @DisplayName("Should handle null username on create via API")
    void shouldHandleNullUsernameOnCreate() {
        String errorMessage = userApiController.createUser(null, "test@example.com");
        assertTrue(errorMessage.contains("Error: User and username cannot be null or empty."));
        assertTrue(userService.getAllUsers().isEmpty());
    }
}