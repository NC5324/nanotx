package com.tusofia.ndurmush.business.user.facade;

import com.tusofia.ndurmush.base.exception.ApplicationException;
import com.tusofia.ndurmush.base.facade.BaseDbFacade;
import com.tusofia.ndurmush.base.facade.PredicateBuilderParams;
import com.tusofia.ndurmush.base.utils.CollectionUtils;
import com.tusofia.ndurmush.base.utils.CriteriaPredicatesBuilder;
import com.tusofia.ndurmush.business.user.UserFilter;
import com.tusofia.ndurmush.business.user.entity.User;
import com.tusofia.ndurmush.business.user.entity.UserRole;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@ApplicationScoped
public class UserDbFacade extends BaseDbFacade {

    @Inject
    private UserRoleDbFacade roleFacade;

    public long count(final UserFilter filter, final User user) {
        return count(createPredicateBuilder(filter, user), User.class, true);
    }

    /**
     * Searches for users by the given ids.
     */
    @SuppressWarnings("unchecked")
    public List<User> findUsersByIds(Collection<Long> ids) {
        return em.createNamedQuery(User.NAMED_QUERY_FIND_BY_IDS).setParameter("ids", ids).getResultList();
    }

    public Optional<User> findUserByLogin(final String login) {
        if (isEmpty(login)) {
            return Optional.empty();
        }
        try {
            final CriteriaBuilder cb = em.getCriteriaBuilder();
            final CriteriaQuery<User> cq = cb.createQuery(User.class);
            final Root<User> root = cq.from(User.class);
            cq.where(cb.equal(cb.upper(root.get("login")), login.toUpperCase()));
            cq.distinct(true);
            return Optional.ofNullable(em.createQuery(cq).getSingleResult());
        } catch (NoResultException | NonUniqueResultException e) {
            return Optional.empty();
        }
    }

    public List<User> findUsersByLogin(final Set<String> logins) {
        if (isEmpty(logins)) {
            return new ArrayList<>();
        }
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<User> cq = cb.createQuery(User.class);
        final Root<User> root = cq.from(User.class);
        CriteriaPredicatesBuilder cpb = new CriteriaPredicatesBuilder(cb, null);
        Set<String> upperCaseLogins = logins.stream().map(String::toUpperCase).collect(Collectors.toSet());
        cpb.in(cb.upper(root.get("login")), upperCaseLogins);
        cq.where(cpb.getPredicatesArray());
        cq.distinct(true);
        return em.createQuery(cq).getResultList();
    }

    public List<User> findUsersByRole(final UserRole role) {
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<User> cq = cb.createQuery(User.class);
        final Root<User> root = cq.from(User.class);
        UserFilter userFilter = new UserFilter();
        userFilter.setRoles(CollectionUtils.asSet(role));
        CriteriaPredicatesBuilder cpb = new CriteriaPredicatesBuilder(cb, null);
        createRolesPredicate(cpb, root, userFilter);
        cq.where(cpb.getPredicatesArray());
        return em.createQuery(cq).getResultList();
    }

    public Optional<User> findUserByEmail(final String email) {
        if (isEmpty(email)) {
            return Optional.empty();
        }
        try {
            final CriteriaBuilder cb = em.getCriteriaBuilder();
            final CriteriaQuery<User> cq = cb.createQuery(User.class);
            final Root<User> root = cq.from(User.class);
            cq.where(cb.equal(cb.upper(root.get("email")), email.toUpperCase()));
            cq.distinct(true);
            final TypedQuery<User> q = em.createQuery(cq);
            return Optional.ofNullable(q.getSingleResult());
        } catch (NoResultException | NonUniqueResultException e) {
            return Optional.empty();
        }
    }

    public User findUserById(long id) {
        return load(id, User.class, true);
    }

    @Transactional
    public User save(final User entity, final User editor) throws ApplicationException {
        return super.save(entity, editor, entity.isNew());
    }

    public List<User> list(final UserFilter filter, final User user) {
        return list(filter.getOffset(), filter.getPageSize(), createPredicateBuilder(filter, user), User.class);
    }

    private Function<PredicateBuilderParams<User>, List<Predicate>> createPredicateBuilder(final UserFilter filter,
                                                                                           final User user) {
        return p -> buildPredicates(p.criteriaPredicateBuilder(), p.root(), p.criteriaQuery(), filter, user);
    }

    private List<Predicate> buildPredicates(final CriteriaPredicatesBuilder cpb, final Root<User> root,
                                            final CriteriaQuery<?> cq, final UserFilter filter, final User user) {
        createFilterByDeletedPredicates(cpb, root, filter);
        createRolesPredicate(cpb, root, filter);
        return cpb.getPredicates();
    }

    private void createRolesPredicate(final CriteriaPredicatesBuilder cpb, final Root<User> root,
                                      final UserFilter filter) {
        if (CollectionUtils.isNotEmpty(filter.getRoles())) {
            final Join<User, Role> rolesJoin = root.join("roles");
            cpb.in(rolesJoin.get("id"), filter.getRoles());
        }
    }

}
