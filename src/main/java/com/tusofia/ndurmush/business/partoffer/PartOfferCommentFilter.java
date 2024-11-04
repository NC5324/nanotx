package com.tusofia.ndurmush.business.partoffer;

import com.tusofia.ndurmush.base.BaseFilter;
import com.tusofia.ndurmush.base.utils.CollectionUtils;

import java.util.Set;

public class PartOfferCommentFilter extends BaseFilter {

    private Set<Long> offerIds;

    private Set<Long> offerRootIds;

    private boolean enrichWithOffer;

    public Set<Long> getOfferIds() {
        return CollectionUtils.copy(offerIds);
    }

    public void setOfferIds(Set<Long> offerIds) {
        this.offerIds = CollectionUtils.copy(offerIds);
    }

    public Set<Long> getOfferRootIds() {
        return CollectionUtils.copy(offerRootIds);
    }

    public void setOfferRootIds(Set<Long> offerRootIds) {
        this.offerRootIds = CollectionUtils.copy(offerRootIds);
    }

    public boolean shouldEnrichWithOffer() {
        return enrichWithOffer;
    }

    public void setEnrichWithOffer(final boolean enrichWithOffer) {
        this.enrichWithOffer = enrichWithOffer;
    }
}
