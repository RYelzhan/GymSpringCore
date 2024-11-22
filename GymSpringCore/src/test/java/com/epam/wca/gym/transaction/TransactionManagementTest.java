package com.epam.wca.gym.transaction;

import com.epam.wca.gym.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class TransactionManagementTest {
    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
        //
    }

    // TODO: finish it
/*    @ParameterizedTest
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

 */

    // TODO: finish it
    @Test
    @Transactional
    @Rollback(value = false)
    void testCommitBehaviour() {

    }
}
