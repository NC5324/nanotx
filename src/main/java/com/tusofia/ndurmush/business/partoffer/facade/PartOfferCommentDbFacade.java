package com.tusofia.ndurmush.business.partoffer.facade;

import com.tusofia.ndurmush.base.exception.ObjectAlreadyModifiedException;
import com.tusofia.ndurmush.base.facade.BaseDbFacade;
import com.tusofia.ndurmush.base.facade.PredicateBuilderParams;
import com.tusofia.ndurmush.base.utils.CollectionUtils;
import com.tusofia.ndurmush.base.utils.CriteriaPredicatesBuilder;
import com.tusofia.ndurmush.base.utils.JpaUtils;
import com.tusofia.ndurmush.business.partoffer.PartOfferCommentFilter;
import com.tusofia.ndurmush.business.partoffer.PartOfferFilter;
import com.tusofia.ndurmush.business.partoffer.entity.PartOffer;
import com.tusofia.ndurmush.business.partoffer.entity.PartOfferComment;
import com.tusofia.ndurmush.business.user.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

@ApplicationScoped
public class PartOfferCommentDbFacade extends BaseDbFacade {

    @Inject
    PartOfferDbFacade offerDbFacade;

    public List<PartOfferComment> save(final List<PartOfferComment> entities, final User editor) {
        List<PartOfferComment> saved = new ArrayList<>();
        for (PartOfferComment entry : CollectionUtils.defaultList(entities)) {
            saved.add(save(entry, editor));
        }
        return saved;
    }

    public PartOfferComment save(final PartOfferComment entity, final User editor) {
        final PartOfferComment entityToSave = JpaUtils.findOrCreateEntity(em, PartOfferComment.class, entity.getId(), true);
        if (entity.getVersion() < entityToSave.getVersion()) {
            ObjectAlreadyModifiedException exception = new ObjectAlreadyModifiedException();
            exception.setCurrentObject(entityToSave);
            throw exception;
        }
        entityToSave.setDeleted(entity.isDeleted());
        entityToSave.setOfferId(entity.getOfferId());
        entityToSave.setParentId(entity.getParentId());
        entityToSave.setContent(entity.getContent());
        return save(entityToSave, editor, entity.isNew());
    }

    public PartOfferComment loadOrNotFound(final Long id, final String notFoundMessage) {
        PartOfferComment loaded = load(id);
        if (loaded == null) {
            throw new IllegalArgumentException(notFoundMessage);
        }
        return loaded;
    }

    public PartOfferComment load(final Long id) {
        return load(id, PartOfferComment.class, true);
    }

    public long count(final PartOfferCommentFilter filter, final User user) {
        return count(createPredicateBuilder(filter, user), PartOfferComment.class, true);
    }

    public List<PartOfferComment> list(final PartOfferCommentFilter filter) {
        return list(filter, null);
    }

    public List<PartOfferComment> list(final PartOfferCommentFilter filter, final User user) {
        List<PartOfferComment> comments = list(filter.getOffset(), filter.getPageSize(), createPredicateBuilder(filter, user), PartOfferComment.class);
        if (filter.shouldEnrichWithOffer()) {
            enrichWithOffer(comments);
        }
        return comments;
    }

    private Function<PredicateBuilderParams<PartOfferComment>, List<Predicate>> createPredicateBuilder(
            final PartOfferCommentFilter filter, final User user) {
        return p -> buildPredicates(p.criteriaPredicateBuilder(), p.root(), p.criteriaQuery(), filter, user);
    }

    protected List<Predicate> buildPredicates(final CriteriaPredicatesBuilder cpb,
                                              final Root<PartOfferComment> root, final CriteriaQuery<?> cq,
                                              final PartOfferCommentFilter filter, final User user) {
        createBasePredicates(cpb, root, PartOfferComment.class, filter);
        createInPredicate(cpb, root.get("offerId"), filter.getOfferIds());
        createOfferRootIdsPredicate(cpb, root, cq, filter);
        return cpb.getPredicates();
    }

    private void createOfferRootIdsPredicate(final CriteriaPredicatesBuilder cpb, final Root<PartOfferComment> root,
                                             final CriteriaQuery<?> cq, final PartOfferCommentFilter filter) {
        if (isNotEmpty(filter.getOfferRootIds())) {
            Root<PartOffer> offerRoot = cq.from(PartOffer.class);
            createEqualsPredicate(cpb, root.get("offerId"), offerRoot.get("id"));
            cpb.in(offerRoot.get("rootId"), filter.getOfferRootIds());
        }
    }

    private List<PartOfferComment> enrichWithOffer(final List<PartOfferComment> comments) {
        Set<Long> offerIds = CollectionUtils.defaultList(comments).stream()
                .map(PartOfferComment::getOfferId).collect(Collectors.toSet());
        PartOfferFilter filter = new PartOfferFilter();
        filter.setIds(offerIds);
        Map<Long, PartOffer> offerMap = offerDbFacade.list(filter).stream()
                .collect(Collectors.toMap(PartOffer::getId, Function.identity(), (a, b) -> a));
        CollectionUtils.defaultList(comments).forEach(comment -> comment.setOffer(offerMap.get(comment.getOfferId())));
        return comments;
    }

}
