package com.epam.wca.gym.repository.impl;

import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.repository.TrainerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class TrainerDAOImpl implements TrainerDAO {
    private Map<Long, Trainer> trainerMap;
    @Autowired
    public void setTrainerMap(Map<Long, Trainer> trainerMap) {
        this.trainerMap = trainerMap;
    }

    public void save(Trainer trainer) {

    }

    public void updateById(long id, Trainer trainer) {

    }

    public Trainer findById(long id) {
        return null;
    }
}
