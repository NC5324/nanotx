package com.tusofia.ndurmush.business.masterdata.facade;

import com.tusofia.ndurmush.base.facade.BaseDbFacade;
import com.tusofia.ndurmush.base.facade.PredicateBuilderParams;
import com.tusofia.ndurmush.base.utils.CollectionUtils;
import com.tusofia.ndurmush.base.utils.CriteriaPredicatesBuilder;
import com.tusofia.ndurmush.business.masterdata.PartMasterDataFilter;
import com.tusofia.ndurmush.business.masterdata.entity.PartMasterData;
import com.tusofia.ndurmush.business.user.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@ApplicationScoped
public class PartMasterDataDbFacade extends BaseDbFacade {

    public List<PartMasterData> save(final List<PartMasterData> entities, final User editor) {
        return CollectionUtils.defaultList(entities).stream()
                .map(entity -> save(entity, editor)).collect(Collectors.toList());
    }

    public PartMasterData save(final PartMasterData entity, final User editor) {
        return save(entity, editor, entity.isNew());
    }

    public PartMasterData loadOrNotFound(final Long id, final String notFoundMessage) {
        PartMasterData loaded = load(id);
        if (loaded == null) {
            throw new IllegalArgumentException(notFoundMessage);
        }
        return loaded;
    }

    public PartMasterData load(final Long id) {
        return load(id, PartMasterData.class, true);
    }

    public long count(final PartMasterDataFilter filter, final User user) {
        return count(createPredicateBuilder(filter, user), PartMasterData.class, true);
    }

    public List<PartMasterData> list(final PartMasterDataFilter filter) {
        return list(filter, null);
    }

    public List<PartMasterData> list(final PartMasterDataFilter filter, final User user) {
        return list(filter.getOffset(), filter.getPageSize(), createPredicateBuilder(filter, user), PartMasterData.class);
    }

    private Function<PredicateBuilderParams<PartMasterData>, List<Predicate>> createPredicateBuilder(
            final PartMasterDataFilter filter, final User user) {
        return p -> buildPredicates(p.criteriaPredicateBuilder(), p.root(), p.criteriaQuery(), filter, user);
    }

    protected List<Predicate> buildPredicates(final CriteriaPredicatesBuilder cpb,
                                              final Root<PartMasterData> root, final CriteriaQuery<?> cq,
                                              final PartMasterDataFilter filter, final User user) {
        createBasePredicates(cpb, root, PartMasterData.class, filter);
        return cpb.getPredicates();
    }
}
