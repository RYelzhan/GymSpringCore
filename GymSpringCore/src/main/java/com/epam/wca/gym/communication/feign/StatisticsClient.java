package com.epam.wca.gym.communication.feign;

import com.epam.wca.common.gymcommon.statistics_dto.TrainerTrainingAddDTO;
import com.epam.wca.common.gymcommon.statistics_dto.TrainerWorkloadSummary;
import com.epam.wca.common.gymcommon.statistics_dto.TrainersTrainingsDeleteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    @DeleteMapping("/trainings")
    void deleteTrainings(@RequestBody TrainersTrainingsDeleteDTO trainingsDeleteDTO);
}
