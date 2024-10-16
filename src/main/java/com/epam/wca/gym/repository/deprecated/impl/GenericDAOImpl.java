package com.epam.wca.gym.repository.deprecated.impl;

import com.epam.wca.gym.repository.deprecated.GenericDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

@Slf4j
public abstract class GenericDAOImpl<T, I> implements GenericDAO<T, I> {
    protected final EntityManager entityManager;
    protected final Class<T> entityClass;

    protected GenericDAOImpl(EntityManager entityManager, Class<T> entityClass) {
        this.entityManager = entityManager;
        this.entityClass = entityClass;
    }

    @Override
    public void save(T entity) {
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            entityManager.persist(entity);

            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw new IllegalArgumentException("Entity was not created");
        }
    }

    @Override
    public void update(T entity) {
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            entityManager.merge(entity);

            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw new IllegalArgumentException("Entity was not updated");
        }
    }

    @Override
    public void deleteById(I id) {
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            T entity = entityManager.find(entityClass, id);
            entityManager.remove(entity);

            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw new IllegalArgumentException("Entity was not deleted");
        }
    }

    @Override
    public T findById(I id) {
        try {
            return entityManager.find(entityClass, id);
        } catch (Exception e) {
            throw new IllegalArgumentException("Entity was not found");
        }
    }

    @Override
    public List<T> findAll() {
        return Collections.unmodifiableList(entityManager.createQuery("SELECT t FROM " + entityClass.getSimpleName() + " t", entityClass)
                .getResultList());
    }
}
