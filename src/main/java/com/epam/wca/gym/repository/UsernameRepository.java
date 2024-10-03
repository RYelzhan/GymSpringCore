package com.epam.wca.gym.repository;

import com.epam.wca.gym.entity.Username;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsernameRepository extends JpaRepository<Username, Long> {
    Username findUsernameByBaseUserName(String baseUsername);
}
