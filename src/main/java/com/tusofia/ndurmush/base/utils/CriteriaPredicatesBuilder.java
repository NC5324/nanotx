package com.tusofia.ndurmush.base.utils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Subquery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

public class CriteriaPredicatesBuilder {

    private final CriteriaBuilder cb;
    private final List<Predicate> predicates;
    private final List<CriteriaPredicatesBuilder> ors = new ArrayList<>();
    private final List<CriteriaPredicatesBuilder> ands = new ArrayList<>();

    public CriteriaPredicatesBuilder(CriteriaBuilder cb) {
        this(cb, new ArrayList<Predicate>());
    }

    public CriteriaPredicatesBuilder(CriteriaBuilder cb, List<Predicate> predicates) {
        this.cb = cb;
        this.predicates = new ArrayList<>(predicates);
    }

    public CriteriaPredicatesBuilder like(final Expression<String> field, final String search) {
        if (StringUtils.isNotBlank(search)) {
            predicates.add(cb.like(cb.lower(field), stringForLikeComparison(search)));
        }
        return this;
    }

    public CriteriaPredicatesBuilder numberLike(final Expression<String> field, final String search,
                                                final String formatMask) {
        if (StringUtils.isNotBlank(search)) {
            Expression<String> convertFunction = cb.function("TO_CHAR", String.class, field,
                    cb.literal(formatMask));
            predicates.add(cb.like(convertFunction, stringForExactLikeComparison(search)));
        }
        return this;
    }

    public CriteriaPredicatesBuilder like(final Expression<String> field, final Collection<String> searchIn) {
        if (CollectionUtils.isNotEmpty(searchIn)) {
            if (searchIn.size() == 1) {
                like(field, searchIn.iterator().next());
            } else {
                final CriteriaPredicatesBuilder or = or();
                for (String search : searchIn) {
                    or.like(field, search);
                }
            }
        }
        return this;
    }

    public CriteriaPredicatesBuilder startsWith(final Expression<String> field, final String search) {
        if (StringUtils.isNotBlank(search)) {
            predicates.add(cb.like(cb.lower(field), stringForStartsWithComparison(search)));
        }
        return this;
    }

    public CriteriaPredicatesBuilder startsWith(final Expression<String> field, final Expression<String> search) {
        predicates.add(cb.like(cb.lower(field), expressionForStartsWithComparison(search)));
        return this;
    }

    public Expression<String> expressionForStartsWithComparison(final Expression<String> expression) {
        return cb.concat(cb.lower(expression), cb.literal(StringUtils.PERCENT));
    }

    public <T> CriteriaPredicatesBuilder eq(final Expression<T> field, final T search) {
        if (search != null) {
            predicates.add(cb.equal(field, search));
        }
        return this;
    }

    public <T> CriteriaPredicatesBuilder eq(final Expression<T> field1, final Expression<T> field2) {
        predicates.add(cb.equal(field1, field2));
        return this;
    }

    public Order asc(final Expression<?> field) {
        return cb.asc(field);
    }

    public Order desc(final Expression<?> field) {
        return cb.desc(field);
    }

    public <T> CriteriaPredicatesBuilder notEqual(final Expression<T> field, final T search) {
        if (search != null) {
            predicates.add(cb.notEqual(field, search));
        }
        return this;
    }

    public <T> CriteriaPredicatesBuilder notEqual(final Expression<T> field1, final Expression<T> field2) {
        predicates.add(cb.notEqual(field1, field2));
        return this;
    }

    public <T> CriteriaPredicatesBuilder exists(Subquery<T> subquery) {
        predicates.add(cb.exists(subquery));
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> CriteriaPredicatesBuilder in(final Expression<T> field, final T... searchIn) {
        if (searchIn == null) {
            return this;
        }
        return in(field, Arrays.asList(searchIn));
    }

    public <T> CriteriaPredicatesBuilder in(final Expression<T> field, final Collection<T> searchIn) {
        if (CollectionUtils.isNotEmpty(searchIn)) {
            if (searchIn.size() == 1) {
                eq(field, searchIn.iterator().next());
            } else if (searchIn.size() <= 1000) {
                predicates.add(field.in(searchIn));
            } else {
                final CriteriaPredicatesBuilder or = or();
                for (final List<T> subSearchIn : CollectionUtils.split(searchIn, 1000)) {
                    or.in(field, subSearchIn);
                }
            }
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> CriteriaPredicatesBuilder notIn(final Expression<T> field, final T... searchIn) {
        return notIn(field, Arrays.asList(searchIn));
    }

    public <T> CriteriaPredicatesBuilder notIn(final Expression<T> field, final Collection<T> searchIn) {
        if (!CollectionUtils.isEmpty(searchIn)) {
            if (searchIn.size() == 1) {
                notEqual(field, searchIn.iterator().next());
            } else if (searchIn.size() <= 1000) {
                predicates.add(cb.not(field.in(searchIn)));
            } else {
                final CriteriaPredicatesBuilder or = or();
                for (final List<T> subSearchIn : CollectionUtils.split(searchIn, 1000)) {
                    or.notIn(field, subSearchIn);
                }
            }
        }
        return this;
    }

    public <T> CriteriaPredicatesBuilder isNotNull(final Expression<T> field) {
        predicates.add(field.isNotNull());
        return this;
    }

    public <T> CriteriaPredicatesBuilder isNull(final Expression<T> field) {
        predicates.add(field.isNull());
        return this;
    }

    public <T extends Collection<?>> CriteriaPredicatesBuilder isEmpty(final Expression<T> collection) {
        predicates.add(cb.isEmpty(collection));
        return this;
    }

    public <T extends Collection<?>> CriteriaPredicatesBuilder isNotEmpty(final Expression<T> collection) {
        predicates.add(cb.isNotEmpty(collection));
        return this;
    }

    public CriteriaPredicatesBuilder equalBool(final Expression<Boolean> field, final Boolean search) {
        if (search != null) {
            if (!search) {
                predicates.add(cb.or(field.isNull(), cb.equal(field, search)));
            } else {
                predicates.add(cb.equal(field, search));
            }
        }
        return this;
    }

    public <T extends Comparable<? super T>> CriteriaPredicatesBuilder greaterThanOrEqualTo(final Expression<T> field, final T search) {
        if (search != null) {
            predicates.add(cb.greaterThanOrEqualTo(field, search));
        }
        return this;
    }

    public <T extends Comparable<? super T>> CriteriaPredicatesBuilder lessThanOrEqualTo(final Expression<T> field, final T search) {
        if (search != null) {
            predicates.add(cb.lessThanOrEqualTo(field, search));
        }
        return this;
    }

    public CriteriaPredicatesBuilder dateBetween(final Expression<? extends Date> field, final Date from, final Date to) {
        dateGreaterThanOrEqualsTo(field, from);
        dateLessThanOrEqualsTo(field, to);
        return this;
    }

    public CriteriaPredicatesBuilder dateLessThanOrEqualsTo(final Expression<? extends Date> field, final Date to) {
        if (to != null) {
            predicates.add(cb.lessThanOrEqualTo(field, to));
        }
        return this;
    }

    public CriteriaPredicatesBuilder dateGreaterThanOrEqualsTo(final Expression<? extends Date> field, final Date from) {
        if (from != null) {
            predicates.add(cb.greaterThanOrEqualTo(field, from));
        }
        return this;
    }

    public CriteriaPredicatesBuilder datesMinDifference(final Integer minDiff, final Expression<? extends Date> fieldFrom, final Expression<? extends Date> fieldTo) {
        if (minDiff != null && fieldFrom != null && fieldTo != null) {
            Expression<Integer> dayFrom = cb.function("trunc", Integer.class, fieldFrom);
            Expression<Integer> dayTo = cb.function("trunc", Integer.class, fieldTo);
            predicates.add(cb.greaterThanOrEqualTo(cb.diff(dayFrom, dayTo), minDiff));
        }
        return this;
    }

    public Expression<Date> toDate(final Expression<String> stringExpression, final String dateFormat) {
        return cb.function("TO_DATE", Date.class, stringExpression, cb.literal(dateFormat));
    }

    public CriteriaPredicatesBuilder or(Predicate... p) {
        predicates.add(cb.or(p));
        return this;
    }

    public CriteriaPredicatesBuilder or() {
        final CriteriaPredicatesBuilder cpbOr = new CriteriaPredicatesBuilder(cb);
        ors.add(cpbOr);
        return cpbOr;
    }

    public String toIgnoreCase(String s) {
        return StringUtils.isEmpty(s) ? null : s.toLowerCase();
    }

    private String replaceSpacesWithWildcard(String s) {
        final String result;
        if (s != null) {
            result = StringUtils.replaceWithSingleSpace(s).replace(' ', '%');
        } else {
            result = null;
        }
        return result;
    }

    public String stringForLikeComparison(String s) {
        return StringUtils.isEmpty(s) ? null : "%" + replaceSpacesWithWildcard(toIgnoreCase(s).trim()) + "%";
    }

    public String stringForExactLikeComparison(String s) {
        return StringUtils.isEmpty(s) ? null : "%" + replaceSpacesWithWildcard(s.trim()) + "%";
    }

    public String stringForStartsWithComparison(String s) {
        return StringUtils.isEmpty(s) ? null : replaceSpacesWithWildcard(toIgnoreCase(s).trim()) + "%";
    }

    public List<Predicate> getPredicates() {
        for (CriteriaPredicatesBuilder cpbOr : ors) {
            or(cpbOr.getPredicatesArray());
        }
        ors.clear();
        for (CriteriaPredicatesBuilder cpbAnd : ands) {
            and(cpbAnd.getPredicatesArray());
        }
        ands.clear();
        return new ArrayList<>(predicates);
    }

    public Predicate[] getPredicatesArray() {
        return getPredicates().toArray(new Predicate[predicates.size()]);
    }

    public CriteriaBuilder getCriteriaBuilder() {
        return cb;
    }

    public CriteriaPredicatesBuilder not(Predicate predicate) {
        predicates.add(cb.not(predicate));
        return this;
    }

    public <T> CriteriaPredicatesBuilder isEqual(final Expression<T> field, final T search) {
        if (search != null && field != null) {
            predicates.add(cb.equal(field, search));
        }
        return this;
    }

    public <T> CriteriaPredicatesBuilder areEqual(final Expression<T> field1, final Expression<T> field2) {
        if (field1 != null && field2 != null) {
            predicates.add(cb.equal(field1, field2));
        }
        return this;
    }

    public <E, C extends Collection<E>> CriteriaPredicatesBuilder isMember(Expression<E> elem,
                                                                           Expression<C> collection) {
        if (null != elem && null != collection) {
            predicates.add(cb.isMember(elem, collection));
        }
        return this;
    }

    public <E, C extends Collection<E>> CriteriaPredicatesBuilder isMember(E elem, Expression<C> collection) {
        if (null != elem && null != collection) {
            predicates.add(cb.isMember(elem, collection));
        }
        return this;
    }

    public CriteriaPredicatesBuilder and() {
        final CriteriaPredicatesBuilder cpbAnd = new CriteriaPredicatesBuilder(cb);
        ands.add(cpbAnd);
        return cpbAnd;
    }

    public CriteriaPredicatesBuilder and(Predicate... p) {
        predicates.add(cb.and(p));
        return this;
    }

    public <T> void in(Expression<T> expression, Expression<T> value) {
        predicates.add(cb.in(expression).value(value));
    }

    public <T extends Comparable<? super T>> CriteriaPredicatesBuilder lessThan(final Expression<T> field,
                                                                                final T search) {
        if (search != null) {
            predicates.add(cb.lessThan(field, search));
        }
        return this;
    }

}
