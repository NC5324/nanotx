package com.tusofia.ndurmush.base.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class JpaUtils {

    public static <T> T findOrCreateEntity(final EntityManager em, final Class<T> entityClass, final Long id, final boolean clearCache) {
        Objects.requireNonNull(entityClass);
        Objects.requireNonNull(em);
        T entity = null;
        if (id != null && id != 0) {
            if (clearCache) {
                JpaUtils.clearCache(em, entityClass, id);
            }
            entity = em.find(entityClass, id);
        }
        if (entity == null) {
            try {
                entity = entityClass.getDeclaredConstructor().newInstance();
            } catch (final InstantiationException | IllegalAccessException ex) {
                throw new IllegalArgumentException("Cannot instantiate entity class " + entityClass.getName(), ex);
            } catch (InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        Objects.requireNonNull(entity);
        return entity;
    }

    public static void clearCache(final EntityManager em, final Class<?> entityClass, final Object entityId) {
        if (entityClass != null && entityId != null) {
            em.getEntityManagerFactory().getCache().evict(entityClass, entityId);
        }
    }

    public static long generateId(final EntityManager em, final String sequenceName) {
        final Query q = em.createNativeQuery(String.format("SELECT nextval('%s')", sequenceName));
        return ((Number) q.getSingleResult()).longValue();
    }

}
