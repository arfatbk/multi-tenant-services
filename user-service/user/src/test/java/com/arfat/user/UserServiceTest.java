package com.arfat.user;


import com.arfat.user.model.UpdateUser;
import com.arfat.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void init() {
        this.userService = new DefaultUserService(new UserRepositoryStub());
    }

    @Test
    void shouldCreateUser() {
        var user = new User("testuser", "password123");

        var createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals("testuser", createdUser.getUsername());
        assertEquals("password123", createdUser.getPassword());
    }

    @Test
    void shouldReturnValidationError() {

        var illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> new User("", "23456754"));
        assertEquals("Username cannot be empty", illegalArgumentException.getMessage());

        illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> new User("asdfghfd", ""));
        assertEquals("Password cannot be empty", illegalArgumentException.getMessage());
    }

    @Test
    void shouldReturnUserNotFound() {
        var userNotFound = assertThrows(UserNotFound.class, () -> userService.findUserByUsername("nonexistentuser"));

        assertEquals("User with username 'nonexistentuser' not found", userNotFound.getMessage());
    }

    @Test
    void shouldReturnUserByUsername() {

        var user = new User("user1", "password1");
        var createdUser = userService.createUser(user);

        var foundUser = userService.findUserByUsername("user1");
        assertNotNull(foundUser);
        assertEquals(createdUser.getUsername(), foundUser.getUsername());
        assertEquals(createdUser.getPassword(), foundUser.getPassword());
    }

    @Test
    void shouldUpdateUser() {
        var user = new User("user1", "password");
        userService.createUser(user);

        var updatedUser = userService.updateUser(
                "user1",
                UpdateUser.builder()
                        .username("user2")
                        .password("changed")
                        .build()
        );

        assertNotNull(updatedUser);
        assertEquals("user2", updatedUser.getUsername());
        assertEquals("changed", updatedUser.getPassword());


        var foundUser = userService.findUserByUsername("user1");
        assertNotNull(foundUser);
        assertEquals("user2", foundUser.getUsername());
        assertEquals("changed", foundUser.getPassword());
    }

    @Test
    void shouldReturnUserNotFoundOnUpdate() {
        var userNotFound = assertThrows(UserNotFound.class, () -> userService.updateUser(
                "nonexistentuser",
                UpdateUser.builder().build()
        ));

        assertEquals("User with username 'nonexistentuser' not found", userNotFound.getMessage());
    }


    @Test
    void shouldReturnUserNotFoundOnDeleteUser() {

        var userNotFound = assertThrows(UserNotFound.class, () -> userService.deleteUser("nonexistentuser"));
        assertEquals("User with username 'nonexistentuser' not found", userNotFound.getMessage());
    }

    @Test
    void shouldDeleteUser() {
        var user = new User("user1", "password");
        userService.createUser(user);

        assertDoesNotThrow(() -> userService.deleteUser("user1"));

    }
}