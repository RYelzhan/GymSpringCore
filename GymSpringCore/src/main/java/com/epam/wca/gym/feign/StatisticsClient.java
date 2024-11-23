package com.epam.wca.gym.feign;

import com.epam.wca.gym.dto.statistics_dto.TrainerTrainingAddDTO;
import com.epam.wca.gym.dto.statistics_dto.TrainerWorkloadSummary;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "Statistics", path = "/trainers")
public interface StatisticsClient {
    @PostMapping("/trainings")
    void addNewTraining(@RequestBody TrainerTrainingAddDTO trainingAddDTO);

    @GetMapping("/trainings/{username}")
    TrainerWorkloadSummary getWorkload(@PathVariable(name = "username") String username);
}
