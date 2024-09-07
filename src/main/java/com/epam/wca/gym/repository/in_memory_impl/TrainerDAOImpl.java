package com.epam.wca.gym.repository.in_memory_impl;

import com.epam.wca.gym.entity.Trainer;
import com.epam.wca.gym.repository.TrainerDAO;

import java.util.Collections;
import java.util.Map;

/**
 * Implementation of {@link TrainerDAO} that uses an in-memory storage (Map) to manage {@link Trainer} entities.
 *
 * <p>This class provides CRUD operations for managing {@link Trainer} objects. It uses maps to store trainers
 * and map usernames to user IDs. This implementation is suitable for testing and small-scale applications where
 * a database is not required. It is not recommended for use in production systems due to its in-memory nature.</p>
 *
 * @deprecated This class is deprecated and replaced by a new implementation using PostgresSQL with JPA and Hibernate.
 * This in-memory solution is no longer appropriate for production systems.
 * @since 1.1
 * @see TrainerDAO
 */

@Deprecated(since = "1.1", forRemoval = false)

public class TrainerDAOImpl implements TrainerDAO {
    private Map<Long, Trainer> trainerMap;
    private Map<String, Long> usernameToId;
    private long[] currentMaxId;

//    @Autowired
    public void setTrainerMap(Map<Long, Trainer> trainerMap) {
        this.trainerMap = trainerMap;
    }

//    @Autowired
    public void setUsernameToTrainee(Map<String, Long> usernameToId) {
        this.usernameToId = usernameToId;
    }

//    @Autowired
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
        // TODO document why this method is empty
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
        return Collections.unmodifiableMap(trainerMap);
    }

    public Map<String, Long> getUsernameToId() {
        return usernameToId;
    }

    public long[] getCurrentMaxId() {
        return currentMaxId;
    }
}
