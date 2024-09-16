package com.epam.wca.gym.repository;

import java.util.List;

public interface GenericDAO<T, I> {
    void save(T entity);

    void update(T entity);

    void deleteById(I id);

    T findById(I id);

    T findByUniqueName(String username);

    List<T> findAll();
}
