package com.epam.wca.gym.repository.impl;

import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.repository.TrainerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TrainerDAOImpl implements TrainerDAO {
    private Map<Long, Trainer> trainerMap;
    private Map<String, Long> usernameToId;
    private long[] currentMaxId;

    @Autowired
    public void setTrainerMap(Map<Long, Trainer> trainerMap) {
        this.trainerMap = trainerMap;
    }

    @Autowired
    public void setUsernameToTrainee(Map<String, Long> usernameToId) {
        this.usernameToId = usernameToId;
    }

    @Autowired
    public void setCurrentMaxId(long[] currentMaxId) {
        this.currentMaxId = currentMaxId;
    }

    public Trainer save(Trainer trainer) {
        trainer.setUserId(++currentMaxId[0]);
        trainerMap.put(currentMaxId[0], trainer);
        usernameToId.put(trainer.getUserName(), currentMaxId[0]);

        return trainer;
    }

    @Override
    public void updateById(Long id, Trainer trainer) {
        trainerMap.put(id, trainer);
    }

    @Override
    public void deleteById(Long id) {
    }

    @Override
    public Trainer findById(Long id) {
        if (trainerMap.containsKey(id)) {
            return trainerMap.get(id);
        }
        return null;
    }

    public void updateByUsername(String username, Trainer trainer) {
        if (usernameToId.containsKey(username) &&
                trainerMap.containsKey(usernameToId.get(username))) {
            trainerMap.put(usernameToId.get(username), trainer);
        }
    }

    public Trainer findByUsername(String username) {
        if (usernameToId.containsKey(username)) {
            return trainerMap.get(usernameToId.get(username));
        }
        return null;
    }

    public Trainer findById(long id) {
        if (trainerMap.containsKey(id)) {
            return trainerMap.get(id);
        }
        return null;
    }

    public Map<Long, Trainer> getAll() {
        return trainerMap;
    }

    public Map<String, Long> getUsernameToId() {
        return usernameToId;
    }

    public long[] getCurrentMaxId() {
        return currentMaxId;
    }
}
