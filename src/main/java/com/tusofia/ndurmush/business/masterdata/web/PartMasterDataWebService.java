package com.tusofia.ndurmush.business.masterdata.web;

import com.tusofia.ndurmush.base.web.BaseWebService;
import com.tusofia.ndurmush.business.masterdata.PartMasterDataFilter;
import com.tusofia.ndurmush.business.masterdata.entity.PartMasterData;
import com.tusofia.ndurmush.business.masterdata.facade.PartMasterDataDbFacade;
import com.tusofia.ndurmush.business.user.entity.User;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Authenticated
@RequestScoped
@Path("master-data")
public class PartMasterDataWebService extends BaseWebService {

    @Inject
    private PartMasterDataDbFacade masterDataDbFacade;

    @POST
    @Path("count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Long count(final PartMasterDataFilter filter) {
        return masterDataDbFacade.count(filter, currentUser());
    }

    @POST
    @Path("list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<PartMasterData> list(final PartMasterDataFilter filter) {
        return masterDataDbFacade.list(filter, currentUser());
    }

    @POST
    @Path("save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({User.ROLE_ADMIN})
    public PartMasterData save(final PartMasterData entity) {
        return masterDataDbFacade.save(entity, currentUser());
    }

    @POST
    @Path("save-multiple")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({User.ROLE_ADMIN})
    public List<PartMasterData> save(final List<PartMasterData> entities) {
        return masterDataDbFacade.save(entities, currentUser());
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public PartMasterData load(@PathParam("id") final long id) {
        return masterDataDbFacade.loadOrNotFound(id, String.format("Entity with ID: %d cannot be allocated", id));
    }

}
