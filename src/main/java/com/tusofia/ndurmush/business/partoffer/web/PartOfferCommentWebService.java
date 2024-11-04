package com.tusofia.ndurmush.business.partoffer.web;

import com.tusofia.ndurmush.base.web.BaseWebService;
import com.tusofia.ndurmush.business.partoffer.PartOfferCommentFilter;
import com.tusofia.ndurmush.business.partoffer.entity.PartOfferComment;
import com.tusofia.ndurmush.business.partoffer.facade.PartOfferCommentDbFacade;
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

@RequestScoped
@Path("comments")
public class PartOfferCommentWebService extends BaseWebService {

    @Inject
    private PartOfferCommentDbFacade partOfferCommentDbFacade;

    @POST
    @Path("count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Long count(final PartOfferCommentFilter filter) {
        return partOfferCommentDbFacade.count(filter, currentUser());
    }

    @POST
    @Path("list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<PartOfferComment> list(final PartOfferCommentFilter filter) {
        return partOfferCommentDbFacade.list(filter, currentUser());
    }

    @POST
    @Path("save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public PartOfferComment save(final PartOfferComment entity) {
        return partOfferCommentDbFacade.save(entity, currentUser());
    }

    @POST
    @Path("save-multiple")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<PartOfferComment> save(final List<PartOfferComment> entities) {
        return partOfferCommentDbFacade.save(entities, currentUser());
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public PartOfferComment load(@PathParam("id") final long id) {
        return partOfferCommentDbFacade.loadOrNotFound(id, String.format("Entity with ID: %d cannot be allocated", id));
    }

}
