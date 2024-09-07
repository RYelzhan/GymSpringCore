package com.epam.wca.gym.repository.in_memory_impl;

import com.epam.wca.gym.entity.Training;
import com.epam.wca.gym.repository.TrainingDAO;

import java.util.Collections;
import java.util.Map;

/**
 * Implementation of {@link TrainingDAO} that uses an in-memory storage (Map) to manage {@link Training} entities.
 *
 * <p>This class provides methods for CRUD operations on {@link Training} objects, utilizing an in-memory map
 * for storage. It is intended for use in scenarios where a full database is not needed, such as in testing or
 * development environments.</p>
 *
 * @deprecated This class is deprecated and should be replaced with an implementation that uses PostgresSQL with JPA
 * and Hibernate. The current in-memory approach is not suitable for production use.
 * @since 1.1
 * @see TrainingDAO
 */

@Deprecated(since = "1.1", forRemoval = false)

public class TrainingDAOImpl implements TrainingDAO {
    private Map<Long, Training> trainingMap;
    private Long[] currentMaxId;

//    @Autowired
    public void setTrainingMap(Map<Long, Training> trainingMap) {
        this.trainingMap = trainingMap;
    }

//    @Autowired
    public void setCurrentMaxId(Long[] currentMaxId) {
        this.currentMaxId = currentMaxId;
    }

    @Override
    public Training save(Training training) {
        training.setTrainingId(++currentMaxId[0]);
        trainingMap.put(currentMaxId[0], training);

        return training;
    }

    @Override
    public void updateById(Long aLong, Training entity) {
        // TODO document why this method is empty
    }

    @Override
    public void deleteById(Long aLong) {
        // TODO document why this method is empty
    }

    @Override
    public Training findById(Long id) {
        if (trainingMap.containsKey(id)) {
            return trainingMap.get(id);
        }
        return null;
    }

    public Map<Long, Training> getAll() {
        return Collections.unmodifiableMap(trainingMap);
    }

    public long getCurrentMaxId() {
        return currentMaxId[0];
    }
}
