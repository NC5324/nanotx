package com.tusofia.ndurmush.base.facade;

import com.tusofia.ndurmush.base.utils.CriteriaPredicatesBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public record PredicateBuilderParams<E>(CriteriaPredicatesBuilder criteriaPredicateBuilder,
                                        CriteriaQuery<?> criteriaQuery, Root<E> root) {
}