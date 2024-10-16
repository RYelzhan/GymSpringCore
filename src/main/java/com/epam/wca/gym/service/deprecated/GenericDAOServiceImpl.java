package com.epam.wca.gym.service.deprecated;

import com.epam.wca.gym.aop.Logging;
import com.epam.wca.gym.repository.deprecated.GenericDAO;

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
        try {
            return genericDAO.findById(id);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    @Logging
    public T findByUniqueName(String uniqueName) {
        try {
            return genericDAO.findByUniqueName(uniqueName);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public List<T> findAll() {
        return genericDAO.findAll();
    }
}
