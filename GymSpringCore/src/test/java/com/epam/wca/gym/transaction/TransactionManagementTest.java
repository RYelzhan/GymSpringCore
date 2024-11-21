package com.epam.wca.gym.transaction;

import com.epam.wca.gym.dto.user.UserUpdateDTO;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TransactionManagementTest {
    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
        //
    }

    // TODO: finish it
    @ParameterizedTest
    @Transactional
    @Sql("data.sql")
    @CsvSource("Test.Transaction")
    void testRollbackBehaviour(String username) {
        User userBeforeUpdate = userService.findByUsername(username);

        UserUpdateDTO userDTO = new UserUpdateDTO(
                "1234567890"
        );

        String oldPassword = userBeforeUpdate.getPassword();

        userService.update(userBeforeUpdate, userDTO);

        User userAfterUpdate = userService.findByUsername(username);

        assertEquals(oldPassword, userAfterUpdate.getPassword());
    }

    // TODO: finish it
    @Test
    @Transactional
    @Rollback(value = false)
    void testCommitBehaviour() {

    }
}
