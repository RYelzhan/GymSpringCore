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
    @Test
    @Transactional
    @Rollback(value = false)
    void testCommitBehaviour() {

    }
}
