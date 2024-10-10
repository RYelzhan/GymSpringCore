package com.epam.wca.gym.repository;

import com.epam.wca.gym.entity.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Long> {
    Trainee findTraineeByUserName(String username);
}
