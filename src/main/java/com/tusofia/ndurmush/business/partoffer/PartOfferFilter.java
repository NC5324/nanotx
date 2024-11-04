package com.tusofia.ndurmush.business.partoffer;

import com.tusofia.ndurmush.base.BaseFilter;
import com.tusofia.ndurmush.base.utils.CollectionUtils;

import java.util.Set;

public class PartOfferFilter extends BaseFilter {

    private Set<Long> ids;

    private Set<Long> requestIds;

    private Set<Long> rootIds;

    public Set<Long> getIds() {
        return CollectionUtils.copy(ids);
    }

    public void setIds(final Set<Long> ids) {
        this.ids = CollectionUtils.copy(ids);
    }

    public Set<Long> getRequestIds() {
        return CollectionUtils.copy(requestIds);
    }

    public void setRequestIds(Set<Long> requestIds) {
        this.requestIds = CollectionUtils.copy(requestIds);
    }

    public Set<Long> getRootIds() {
        return CollectionUtils.copy(rootIds);
    }

    public void setRootIds(Set<Long> rootIds) {
        this.rootIds = CollectionUtils.copy(rootIds);
    }
}
