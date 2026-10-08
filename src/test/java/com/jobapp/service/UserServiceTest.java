package com.jobapp.service;

import com.jobapp.dao.UserDAOInMemoryImpl;
import com.jobapp.exception.InvalidInputException;
import com.jobapp.model.Role;
import com.jobapp.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = new UserService(new UserDAOInMemoryImpl());
    }

    @Test
    @DisplayName("Test Register New User")
    public void testRegisterUserSuccess() throws Exception {
        User user = userService.registerUser("Alice Testing", "alice@example.com", "pass123", Role.APPLICANT, "9999999999");
        assertNotNull(user);
        assertTrue(user.getId() > 0);
        assertEquals("Alice Testing", user.getName());
    }

    @Test
    @DisplayName("Test Duplicate Email Registration Throws Exception")
    public void testDuplicateEmail() throws Exception {
        userService.registerUser("Bob", "bob@example.com", "pass123", Role.APPLICANT, "1234567890");
        assertThrows(InvalidInputException.class, () -> {
            userService.registerUser("Bob Copy", "bob@example.com", "pass456", Role.APPLICANT, "0987654321");
        });
    }

    @Test
    @DisplayName("Test Login Success")
    public void testLoginSuccess() throws Exception {
        userService.registerUser("Charlie", "charlie@example.com", "secret", Role.RECRUITER, "5555555555");
        User loggedIn = userService.login("charlie@example.com", "secret");
        assertNotNull(loggedIn);
        assertEquals("Charlie", loggedIn.getName());
    }
}
