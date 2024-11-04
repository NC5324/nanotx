package com.tusofia.ndurmush.business.user;

import com.tusofia.ndurmush.base.BaseFilter;
import com.tusofia.ndurmush.base.utils.CollectionUtils;
import com.tusofia.ndurmush.business.user.entity.UserRole;

import java.util.Set;

public class UserFilter extends BaseFilter {

    private Set<UserRole> roles;

    public Set<UserRole> getRoles() {
        return CollectionUtils.copy(roles);
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = CollectionUtils.copy(roles);
    }

}
