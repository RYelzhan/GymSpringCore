package com.epam.wca.gym.repository;

import com.epam.wca.gym.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUserName(String username);
}
