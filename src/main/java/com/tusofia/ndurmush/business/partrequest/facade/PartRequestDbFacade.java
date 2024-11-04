package com.tusofia.ndurmush.business.partrequest.facade;

import com.tusofia.ndurmush.base.entity.status.StatusChangeEmailEventProducer;
import com.tusofia.ndurmush.base.exception.ObjectAlreadyModifiedException;
import com.tusofia.ndurmush.base.facade.BaseDbFacade;
import com.tusofia.ndurmush.base.facade.PredicateBuilderParams;
import com.tusofia.ndurmush.base.utils.CollectionUtils;
import com.tusofia.ndurmush.base.utils.CriteriaPredicatesBuilder;
import com.tusofia.ndurmush.base.utils.JpaUtils;
import com.tusofia.ndurmush.business.partoffer.PartOfferFilter;
import com.tusofia.ndurmush.business.partoffer.entity.PartOffer;
import com.tusofia.ndurmush.business.partoffer.facade.PartOfferDbFacade;
import com.tusofia.ndurmush.business.partrequest.PartRequestFilter;
import com.tusofia.ndurmush.business.partrequest.PartRequestState;
import com.tusofia.ndurmush.business.partrequest.entity.PartRequest;
import com.tusofia.ndurmush.business.user.entity.User;
import com.tusofia.ndurmush.business.user.entity.UserRole;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@ApplicationScoped
public class PartRequestDbFacade extends BaseDbFacade {

    @Inject
    private PartOfferDbFacade partOfferDbFacade;

    public List<PartRequest> save(final List<PartRequest> entities, final User editor) {
        List<PartRequest> saved = new ArrayList<>();
        for (PartRequest entry : CollectionUtils.defaultList(entities)) {
            saved.add(save(entry, editor));
        }
        return saved;
    }

    @StatusChangeEmailEventProducer
    public PartRequest save(PartRequest entity, User editor) {
        PartRequest entityToSave = JpaUtils.findOrCreateEntity(em, PartRequest.class, entity.getId(), true);
        if (entity.getVersion() < entityToSave.getVersion()) {
            ObjectAlreadyModifiedException exception = new ObjectAlreadyModifiedException();
            exception.setCurrentObject(entityToSave);
            throw exception;
        }
        entityToSave.setDeleted(entity.isDeleted());
        entityToSave.setPartNumber(entity.getPartNumber());
        entityToSave.setAlternatePartNumber(entity.getAlternatePartNumber());
        entityToSave.setComponentName(entity.getComponentName());
        entityToSave.setManufacturer(entity.getManufacturer());
        entityToSave.setCategory(entity.getCategory());
        entityToSave.setMinTemp(entity.getMinTemp());
        entityToSave.setMaxTemp(entity.getMaxTemp());
        entityToSave.setCurrency(entity.getCurrency());
        entityToSave.setTargetPrice(entity.getTargetPrice());
        entityToSave.setState(entity.getState());
        entityToSave.setQuantity(entity.getQuantity());
        entityToSave.setComponentType(entity.getComponentType());
        entityToSave.setDocuments(entity.getDocuments());
        return save(entityToSave, editor, entityToSave.isNew());
    }

    public PartRequest loadOrNotFound(final Long id, final String notFoundMessage) {
        PartRequest loaded = load(id);
        if (loaded == null) {
            throw new IllegalArgumentException(notFoundMessage);
        }
        return loaded;
    }

    public PartRequest load(final Long id) {
        return load(id, PartRequest.class, true);
    }

    public long count(final PartRequestFilter filter, final User user) {
        return count(createPredicateBuilder(filter, user), PartRequest.class, true);
    }

    public List<PartRequest> list(final PartRequestFilter filter) {
        return list(filter, null);
    }

    public List<PartRequest> list(final PartRequestFilter filter, final User user) {
        List<PartRequest> partRequests = list(filter.getOffset(), filter.getPageSize(), createPredicateBuilder(filter, user), PartRequest.class);
        enrichWithOffers(partRequests, user);
        return partRequests;
    }

    private Function<PredicateBuilderParams<PartRequest>, List<Predicate>> createPredicateBuilder(
            final PartRequestFilter filter, final User user) {
        return p -> buildPredicates(p.criteriaPredicateBuilder(), p.root(), p.criteriaQuery(), filter, user);
    }

    protected List<Predicate> buildPredicates(final CriteriaPredicatesBuilder cpb,
                                              final Root<PartRequest> root, final CriteriaQuery<?> cq,
                                              final PartRequestFilter filter, final User user) {
        createBasePredicates(cpb, root, PartRequest.class, filter);
        createUserVisibilityPredicate(cpb, root, user);
        return cpb.getPredicates();
    }

    private void createUserVisibilityPredicate(final CriteriaPredicatesBuilder cpb, final Root<PartRequest> root, final User user) {
        if (Objects.nonNull(user) && UserRole.BUYER.equals(user.getRole())) {
            createEqualsPredicate(cpb, root.get("creator").get("id"), user.getId());
        }
        if (Objects.nonNull(user) && UserRole.SUPPLIER.equals(user.getRole())) {
            createNotInPredicate(cpb, root.get("state"), CollectionUtils.asSet(PartRequestState.OPEN));
        }
    }

    private List<PartRequest> enrichWithOffers(final List<PartRequest> partRequests, final User user) {
        Set<Long> requestIds = CollectionUtils.defaultList(partRequests).stream().map(PartRequest::getId).collect(Collectors.toSet());
        if (isEmpty(requestIds)) {
            return Collections.emptyList();
        }
        PartOfferFilter filter = new PartOfferFilter();
        filter.setRequestIds(requestIds);
        filter.setNullField("nextVersionId");
        Map<Long, List<PartOffer>> partOffers = partOfferDbFacade.list(filter, user).stream()
                .collect(Collectors.groupingBy(PartOffer::getRequestId));
        partRequests.forEach(partRequest -> {
            if (partOffers.containsKey(partRequest.getId())) {
                partRequest.setOffers(partOffers.get(partRequest.getId()));
            }
        });
        return partRequests;
    }

}
