package com.tusofia.ndurmush.business.partrequest.web;

import com.tusofia.ndurmush.base.web.BaseWebService;
import com.tusofia.ndurmush.business.partrequest.PartRequestFilter;
import com.tusofia.ndurmush.business.partrequest.entity.PartRequest;
import com.tusofia.ndurmush.business.partrequest.facade.PartRequestDbFacade;
import io.quarkus.security.Authenticated;
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
@Path("/part-request")
public class PartRequestWebService extends BaseWebService {

    @Inject
    private PartRequestDbFacade partRequestDbFacade;

    @POST
    @Path("count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Long count(final PartRequestFilter filter) {
        return partRequestDbFacade.count(filter, currentUser());
    }

    @POST
    @Path("list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<PartRequest> list(final PartRequestFilter filter) {
        return partRequestDbFacade.list(filter, currentUser());
    }

    @POST
    @Path("save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public PartRequest save(final PartRequest entity) {
        return partRequestDbFacade.save(entity, currentUser());
    }

    @POST
    @Path("save-multiple")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<PartRequest> save(final List<PartRequest> entities) {
        return partRequestDbFacade.save(entities, currentUser());
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public PartRequest load(@PathParam("id") final long id) {
        return partRequestDbFacade.loadOrNotFound(id, String.format("Entity with ID: %d cannot be allocated", id));
    }

}
