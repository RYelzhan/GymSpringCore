package com.epam.wca.gym.repository;

public interface GenericDAO<T, ID> {
    T save(T entity);
    void updateById(ID id, T entity);
    void deleteById(ID id);
    T findById(ID id);
}
