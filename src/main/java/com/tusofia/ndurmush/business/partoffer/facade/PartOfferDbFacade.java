package com.tusofia.ndurmush.business.partoffer.facade;

import com.tusofia.ndurmush.base.entity.status.StatusChangeEmailEventProducer;
import com.tusofia.ndurmush.base.exception.ObjectAlreadyModifiedException;
import com.tusofia.ndurmush.base.facade.BaseDbFacade;
import com.tusofia.ndurmush.base.facade.PredicateBuilderParams;
import com.tusofia.ndurmush.base.utils.CollectionUtils;
import com.tusofia.ndurmush.base.utils.CriteriaPredicatesBuilder;
import com.tusofia.ndurmush.base.utils.JpaUtils;
import com.tusofia.ndurmush.business.partoffer.PartOfferFilter;
import com.tusofia.ndurmush.business.partoffer.PartOfferState;
import com.tusofia.ndurmush.business.partoffer.entity.PartOffer;
import com.tusofia.ndurmush.business.user.entity.User;
import com.tusofia.ndurmush.business.user.entity.UserRole;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@ApplicationScoped
public class PartOfferDbFacade extends BaseDbFacade {

    public List<PartOffer> save(final List<PartOffer> entities, final User editor) {
        List<PartOffer> saved = new ArrayList<>();
        for (PartOffer entry : CollectionUtils.defaultList(entities)) {
            saved.add(save(entry, editor));
        }
        return saved;
    }

    @StatusChangeEmailEventProducer
    public PartOffer save(final PartOffer entity, final User editor) {
        PartOffer entityToSave = JpaUtils.findOrCreateEntity(em, PartOffer.class, entity.getId(), true);
        if (entity.getVersion() < entityToSave.getVersion()) {
            ObjectAlreadyModifiedException exception = new ObjectAlreadyModifiedException();
            exception.setCurrentObject(entityToSave);
            throw exception;
        }
        setIdAndRootId(entityToSave, entity);
        entityToSave.setDeleted(entity.isDeleted());
        entityToSave.setRequestId(entity.getRequestId());
        entityToSave.setPreviousVersionId(entity.getPreviousVersionId());
        entityToSave.setNextVersionId(entity.getNextVersionId());
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
        entityToSave.setOrderDetails(entity.getOrderDetails());
        PartOffer savedOffer = save(entityToSave, editor, entityToSave.isNew());
        savePreviousVersion(savedOffer, editor);
        return savedOffer;
    }

    private void setIdAndRootId(final PartOffer entityToSave, final PartOffer entity) {
        if (entityToSave.isNew()) {
            Long id = JpaUtils.generateId(em, PartOffer.ID_SEQUENCE_NAME);
            entityToSave.setId(id);
            if (Objects.isNull(entity.getRootId())) {
                entityToSave.setRootId(id);
            } else {
                entityToSave.setRootId(entity.getRootId());
            }
        } else {
            entityToSave.setRootId(entity.getRootId());
        }
    }

    private void savePreviousVersion(final PartOffer entity, final User editor) {
        PartOffer previousVersion = load(entity.getPreviousVersionId());
        if (Objects.nonNull(previousVersion)) {
            previousVersion.setDeleted(entity.isDeleted());
            previousVersion.setNextVersionId(entity.getId());
            previousVersion.setRootId(entity.getRootId());
            save(previousVersion, editor, previousVersion.isNew());
        }
    }

    public PartOffer loadOrNotFound(final Long id, final String notFoundMessage) {
        PartOffer loaded = load(id);
        if (loaded == null) {
            throw new IllegalArgumentException(notFoundMessage);
        }
        return loaded;
    }

    public PartOffer load(final Long id) {
        return load(id, PartOffer.class, true);
    }

    public long count(final PartOfferFilter filter, final User user) {
        return count(createPredicateBuilder(filter, user), PartOffer.class, true);
    }

    public List<PartOffer> list(final PartOfferFilter filter) {
        return list(filter, null);
    }

    public List<PartOffer> list(final PartOfferFilter filter, final User user) {
        return list(filter.getOffset(), filter.getPageSize(), createPredicateBuilder(filter, user), PartOffer.class);
    }

    private Function<PredicateBuilderParams<PartOffer>, List<Predicate>> createPredicateBuilder(
            final PartOfferFilter filter, final User user) {
        return p -> buildPredicates(p.criteriaPredicateBuilder(), p.root(), p.criteriaQuery(), filter, user);
    }

    protected List<Predicate> buildPredicates(final CriteriaPredicatesBuilder cpb,
                                              final Root<PartOffer> root, final CriteriaQuery<?> cq,
                                              final PartOfferFilter filter, final User user) {
        createBasePredicates(cpb, root, PartOffer.class, filter);
        createInPredicate(cpb, root.get("rootId"), filter.getRootIds());
        createUserVisibilityPredicate(cpb, root, user);
        return cpb.getPredicates();
    }

    private void createUserVisibilityPredicate(final CriteriaPredicatesBuilder cpb, final Root<PartOffer> root, final User user) {
        if (Objects.nonNull(user) && UserRole.SUPPLIER.equals(user.getRole())) {
            createEqualsPredicate(cpb, root.get("creator").get("id"), user.getId());
        }
        if (Objects.nonNull(user) && UserRole.BUYER.equals(user.getRole())) {
            createNotInPredicate(cpb, root.get("state"), CollectionUtils.asSet(PartOfferState.OPEN, PartOfferState.WITHDRAWN));
        }
    }

}
