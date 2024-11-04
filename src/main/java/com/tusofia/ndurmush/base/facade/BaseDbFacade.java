package com.tusofia.ndurmush.base.facade;

import com.tusofia.ndurmush.base.BaseFilter;
import com.tusofia.ndurmush.base.entity.BaseEntity;
import com.tusofia.ndurmush.base.utils.CollectionUtils;
import com.tusofia.ndurmush.base.utils.CriteriaPredicatesBuilder;
import com.tusofia.ndurmush.base.utils.JpaUtils;
import com.tusofia.ndurmush.business.user.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@BaseDbFacadeImpl
@ApplicationScoped
public class BaseDbFacade {

    @Inject
    protected EntityManager em;

    @Transactional
    protected <E> E save(final E entity, final User user, final boolean isNew) {
        final E savedEntity;
        if (BaseEntity.class.isAssignableFrom(entity.getClass())) {
            touch((BaseEntity) entity, user);
        }
        if (isNew) {
            em.persist(entity);
            savedEntity = entity;
        } else {
            savedEntity = em.merge(entity);
        }
        em.flush();
        return savedEntity;
    }

    private <E extends BaseEntity> void touch(final E entity, final User editor) {
        final Date now = new Date();

        if (entity.getCreated() == null) {
            entity.setCreator(editor);
            entity.setCreated(now);
        }

        entity.setEditor(editor);
        entity.setEdited(now);
    }


    public <E> E load(final Object id, final Class<E> entityClass, final boolean clearCache) {
        if (null == id) {
            return null;
        }
        if (clearCache) {
            JpaUtils.clearCache(em, entityClass, id);
        }
        return em.find(entityClass, id);
    }

    protected <E> long count(final Function<PredicateBuilderParams<E>, List<Predicate>> predicateBuilder, final Class<E> entityClass,
                             boolean distinct) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<E> root = cq.from(entityClass);
        if (distinct) {
            cq.select(cb.countDistinct(root));
        } else {
            cq.select(cb.count(root));
        }
        cq.where(buildPredicates(predicateBuilder, cb, cq, root));
        return em.createQuery(cq).getSingleResult();
    }

    protected <E> List<E> list(final int offset, final int pageSize,
                               final Function<PredicateBuilderParams<E>, List<Predicate>> predicateBuilder,
                               final Class<E> entityClass) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<E> cq = cb.createQuery(entityClass);
        Root<E> root = cq.from(entityClass);
        cq.distinct(true);
        cq.select(root);
        cq.where(buildPredicates(predicateBuilder, cb, cq, root));
        TypedQuery<E> query = em.createQuery(cq);
        limitResults(query, offset, pageSize);
        return CollectionUtils.defaultList(query.getResultList());
    }

    protected <E> Predicate[] buildPredicates(
            final Function<PredicateBuilderParams<E>, List<Predicate>> predicateBuilder, final CriteriaBuilder cb,
            CriteriaQuery<?> cq, final Root<E> root) {
        if (null != predicateBuilder) {
            List<Predicate> predicates = predicateBuilder
                    .apply(new PredicateBuilderParams<E>(new CriteriaPredicatesBuilder(cb), cq, root));
            return predicates.toArray(new Predicate[0]);
        }
        return new Predicate[0];
    }

    protected void limitResults(final TypedQuery<?> query, final int offset, final int pageSize) {
        if (offset > 0) {
            query.setFirstResult(offset);
        }
        if (pageSize > -1) {
            query.setMaxResults(pageSize);
        }
    }

    protected <E> void createBasePredicates(final CriteriaPredicatesBuilder cpb,
                                            final Root<E> root, final Class<E> entityClass, final BaseFilter filter) {
        createNullFieldPredicate(cpb, root, filter);
        createNotNullFieldPredicate(cpb, root, filter);
        if (BaseEntity.class.isAssignableFrom(entityClass)) {
            createFilterByDeletedPredicates(cpb, root, filter);
        }
    }

    protected <T> void createFilterByDeletedPredicates(final CriteriaPredicatesBuilder cpb,
                                                       final Root<T> root, final BaseFilter filter) {
        cpb.equalBool(root.get("deleted"), filter.isDeleted());
    }

    protected <T> void createNullFieldPredicate(final CriteriaPredicatesBuilder cpb,
                                                final Root<T> root, final BaseFilter filter) {
        if (StringUtils.isNotBlank(filter.getNullField())) {
            cpb.isNull(root.get(filter.getNullField()));
        }
    }

    protected <T> void createNotNullFieldPredicate(final CriteriaPredicatesBuilder cpb,
                                                   final Root<T> root, final BaseFilter filter) {
        if (StringUtils.isNotBlank(filter.getNotNullField())) {
            cpb.isNotNull(root.get(filter.getNotNullField()));
        }
    }

    protected <T> void createLikePredicate(final CriteriaPredicatesBuilder cpb,
                                           final Root<T> root, final SingularAttribute<T, String> field, final String search) {
        createLikePredicate(cpb, root.get(field), search);
    }

    protected void createLikePredicate(final CriteriaPredicatesBuilder cpb, final Expression<String> expression, final String search) {
        if (StringUtils.isNotBlank(search)) {
            cpb.like(expression, search);
        }
    }

    protected void createStartsWithPredicate(final CriteriaPredicatesBuilder cpb, final Expression<String> expression, final String search) {
        if (StringUtils.isNotBlank(search)) {
            cpb.startsWith(expression, search);
        }
    }

    protected <T extends BaseEntity> void createEqualsPredicate(final CriteriaPredicatesBuilder cpb,
                                                                final Root<T> root, final SingularAttribute<T, String> field, final String search) {
        if (StringUtils.isNotBlank(search)) {
            cpb.eq(root.get(field), search);
        }
    }

    protected <T extends BaseEntity, V> void createEqualsPredicate(final CriteriaPredicatesBuilder cpb,
                                                                   final Expression<V> expression, final V search) {

        boolean shouldFilter = search instanceof String
                ? StringUtils.isNotBlank((String) search)
                : search != null;

        if (shouldFilter) {
            cpb.eq(expression, search);
        }
    }

    protected <T extends BaseEntity> void createInPredicate(final CriteriaPredicatesBuilder cpb,
                                                            final Root<T> root, final SingularAttribute<T, String> field, final Collection<String> searchIn) {
        if (CollectionUtils.isNotEmpty(searchIn)) {
            cpb.in(root.get(field), searchIn);
        }
    }

    protected <T extends BaseEntity, V> void createNotInPredicate(final CriteriaPredicatesBuilder cpb,
                                                                  final Root<T> root, final SingularAttribute<T, V> field, final Collection<V> searchIn) {
        if (CollectionUtils.isNotEmpty(searchIn)) {
            cpb.notIn(root.get(field), searchIn);
        }
    }

    protected <T extends BaseEntity, V> void createInPredicate(final CriteriaPredicatesBuilder cpb,
                                                               final Expression<V> expression, final Collection<V> searchIn) {
        if (CollectionUtils.isNotEmpty(searchIn)) {
            cpb.in(expression, searchIn);
        }
    }

    protected <T extends BaseEntity, V> void createNotInPredicate(final CriteriaPredicatesBuilder cpb,
                                                                  final Expression<V> expression, final Collection<V> searchIn) {
        if (CollectionUtils.isNotEmpty(searchIn)) {
            cpb.notIn(expression, searchIn);
        }
    }
}
