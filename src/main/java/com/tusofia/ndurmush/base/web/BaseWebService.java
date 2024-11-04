package com.tusofia.ndurmush.base.web;

import com.tusofia.ndurmush.business.user.entity.User;
import com.tusofia.ndurmush.business.user.facade.UserDbFacade;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;

import java.security.Principal;
import java.util.Optional;

public abstract class BaseWebService {

    protected static final String CACHE_CURRENT_USER = "cache.current-user";

    @Context
    SecurityContext securityContext;

    @Inject
    protected UserDbFacade userDbFacade;

    protected User currentUser() {
        return Optional.ofNullable(currentSecurityContext().getUserPrincipal()).map(Principal::getName).flatMap(userDbFacade::findUserByLogin).orElse(null);
    }

    protected SecurityContext currentSecurityContext() {
        return securityContext;
    }

}
