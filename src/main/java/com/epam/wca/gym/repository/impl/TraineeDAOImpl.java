package com.epam.wca.gym.repository.impl;

import com.epam.wca.gym.entity.Trainee;
import com.epam.wca.gym.repository.TraineeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
public class TraineeDAOImpl implements TraineeDAO {
    private Map<Long, Trainee> traineeMap;
    private Map<String, Long> usernameToId;
    private long[] currentMaxId;
    @Autowired
    public void setTraineeMap(Map<Long, Trainee> traineeMap) {
        this.traineeMap = traineeMap;
    }
    @Autowired
    public void setUsernameToId(Map<String, Long> usernameToId) {
        this.usernameToId = usernameToId;
    }
    @Autowired
    public void setCurrentMaxId(long[] currentMaxId) {
        this.currentMaxId = currentMaxId;
    }
    @PostConstruct
    public void updateMaxIdUsed() {
        if (!traineeMap.isEmpty()) {
            currentMaxId[0] = Math.max(currentMaxId[0], traineeMap.keySet().stream().max(Long::compare).get());
        }
    }
    public void save(Trainee trainee) {
        trainee.setUserId(++ currentMaxId[0]);
        traineeMap.put(currentMaxId[0], trainee);
        usernameToId.put(trainee.getUserName(), currentMaxId[0]);
    }

    public void updateByUsername(String username, Trainee trainee) {
        if (usernameToId.containsKey(username) && traineeMap.containsKey(usernameToId.get(username))) {
            traineeMap.put(usernameToId.get(username), trainee);
        }
    }

    public void deleteByUsername(String username) {
        if (usernameToId.containsKey(username)) {
            traineeMap.remove(usernameToId.get(username));
        }
    }

    public Trainee findByUsername(String username) {
        if (usernameToId.containsKey(username)) {
            return traineeMap.get(usernameToId.get(username));
        }
        return null;
    }

    public Map<Long, Trainee> getAll() {
        return traineeMap;
    }
    public Map<String, Long> getUsernameToId() {
        return usernameToId;
    }
    public long[] getCurrentMaxId() {
        return currentMaxId;
    }
}
