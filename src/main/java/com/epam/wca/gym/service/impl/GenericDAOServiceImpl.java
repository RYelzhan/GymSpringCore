package com.epam.wca.gym.service.impl;

import com.epam.wca.gym.repository.GenericDAO;
import com.epam.wca.gym.service.GenericDAOService;

import java.util.List;

public abstract class GenericDAOServiceImpl<T, K, I> implements GenericDAOService<T, K, I> {
    protected final GenericDAO<T, I> genericDAO;

    protected GenericDAOServiceImpl(GenericDAO<T, I> genericDAO) {
        this.genericDAO = genericDAO;
    }

    @Override
    public void update(T entity) {
        genericDAO.update(entity);
    }

    @Override
    public void deleteById(I id) {
        genericDAO.deleteById(id);
    }

    @Override
    public T findById(I id) {
        return genericDAO.findById(id);
    }

    @Override
    public T findByUniqueName(String uniqueName) {
        return genericDAO.findByUniqueName(uniqueName);
    }

    @Override
    public List<T> findAll() {
        return genericDAO.findAll();
    }
}
