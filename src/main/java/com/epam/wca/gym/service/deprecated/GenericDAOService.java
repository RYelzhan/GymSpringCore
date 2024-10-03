package com.epam.wca.gym.service.deprecated;

import java.util.List;

public interface GenericDAOService<T, K, I> {
    T save(K dto);

    void update(T entity);

    void deleteById(I id);

    T findById(I id);

    T findByUniqueName(String uniqueName);

    List<T> findAll();
}
