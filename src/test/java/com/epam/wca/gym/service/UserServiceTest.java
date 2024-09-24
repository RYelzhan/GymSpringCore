package com.epam.wca.gym.service;

import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.service.impl.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void testUnsupportedSave() {
        // Verify that save() throws UnsupportedOperationException
        assertThrows(UnsupportedOperationException.class, () -> userService.save(user));
    }

    @Test
    void testUnsupportedUpdate() {
        // Verify that update() throws UnsupportedOperationException
        assertThrows(UnsupportedOperationException.class, () -> userService.update(user));
    }

    @Test
    void testUnsupportedDeleteById() {
        // Verify that deleteById() throws UnsupportedOperationException
        assertThrows(UnsupportedOperationException.class, () -> userService.deleteById(1L));
    }

    @Test
    void testUnsupportedFindById() {
        // Verify that findById() throws UnsupportedOperationException
        assertThrows(UnsupportedOperationException.class, () -> userService.findById(1L));
    }
}
