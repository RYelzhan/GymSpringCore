package com.epam.wca.gym.service.deprecated;

import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.service.deprecated.UserServiceOld;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UserServiceOldTest {
    @InjectMocks
    private UserServiceOld userServiceOld;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void testUnsupportedSave() {
        // Verify that save() throws UnsupportedOperationException
        assertThrows(UnsupportedOperationException.class, () -> userServiceOld.save(user));
    }

    @Test
    void testUnsupportedDeleteById() {
        // Verify that deleteById() throws UnsupportedOperationException
        assertThrows(UnsupportedOperationException.class, () -> userServiceOld.deleteById(1L));
    }

    @Test
    void testUnsupportedFindById() {
        // Verify that findById() throws UnsupportedOperationException
        assertThrows(UnsupportedOperationException.class, () -> userServiceOld.findById(1L));
    }
}
