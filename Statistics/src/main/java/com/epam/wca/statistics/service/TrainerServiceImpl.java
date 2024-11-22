package com.epam.wca.statistics.service;

import com.epam.wca.statistics.dto.TrainerTrainingAddDTO;
import com.epam.wca.statistics.dto.TrainerWorkloadSummary;
import com.epam.wca.statistics.entity.TrainerWorkload;
import com.epam.wca.statistics.repository.TrainerWorkloadRepository;
import com.epam.wca.statistics.util.DTOFactory;
import com.epam.wca.statistics.util.EntityFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService{
    private final TrainerWorkloadRepository trainerWorkloadRepository;

    @Override
    @Transactional
    public void addNewTraining(TrainerTrainingAddDTO trainingAddDTO) {
        var trainerWorkload = trainerWorkloadRepository.findByUsernameAndYearAndMonth(
                trainingAddDTO.username(),
                trainingAddDTO.date().getYear(),
                trainingAddDTO.date().getMonthValue()
        ).orElse(EntityFactory.covertToZeroDurationEntity(trainingAddDTO));

        trainerWorkload.setDuration(trainerWorkload.getDuration() + trainingAddDTO.duration());

        trainerWorkloadRepository.save(trainerWorkload);
    }

    @Override
    public TrainerWorkloadSummary getWorkload(String username) {
        List<TrainerWorkload> trainerWorkloads = trainerWorkloadRepository.findAllByUsername(username);

        Map<Integer, Map<Integer, Integer>> trainingSummary = new HashMap<>();

        trainerWorkloads.forEach(trainerWorkload ->
                trainingSummary
                        .computeIfAbsent(trainerWorkload.getYear(), HashMap::new)
                        .merge(trainerWorkload.getMonth(), trainerWorkload.getDuration(), Integer::sum));

        return DTOFactory.convertToDto(username, trainingSummary);
    }
}
