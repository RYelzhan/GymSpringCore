package com.epam.wca.authentication.repository;

import com.epam.wca.authentication.entity.Username;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsernameRepository extends JpaRepository<Username, Long> {
    Optional<Username> findUsernameByBaseUserName(String baseUsername);
}
