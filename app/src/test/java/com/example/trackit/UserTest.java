package com.example.trackit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class UserTest {
    private User mockUser()
    {
        String userName = "test_user";
        String password = "Password";
        return new User(userName, password);
    }

    @Test
    void testUsername()
    {
        User user = mockUser();
        assertEquals("test_user", user.getUsername());
        user.setUsername("user1");
        assertEquals("user1", user.getUsername());
    }
    @Test
    void testPassword()
    {
        User user = mockUser();
        assertEquals("Password", user.getPassword());
        user.setUsername("password");
        assertEquals("password", user.getPassword());
    }
}
