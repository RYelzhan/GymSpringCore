package com.epam.wca.gym.repository.impl;

import com.epam.wca.gym.repository.GenericDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.extern.slf4j.Slf4j;

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

            System.out.println(entity);

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
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            T entity = entityManager.find(entityClass, id);

            entityManager.detach(entity);

            transaction.commit();

            return entity;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw new IllegalArgumentException("Entity was not found");
        }
    }

    @Override
    public List<T> findAll() {
        return entityManager.createQuery("SELECT t FROM " + entityClass.getSimpleName() + " t", entityClass)
                .getResultList();
    }
}
