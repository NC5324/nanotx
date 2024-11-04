package com.tusofia.ndurmush.business.partoffer.web;

import com.tusofia.ndurmush.base.web.BaseWebService;
import com.tusofia.ndurmush.business.partoffer.PartOfferFilter;
import com.tusofia.ndurmush.business.partoffer.entity.PartOffer;
import com.tusofia.ndurmush.business.partoffer.facade.PartOfferDbFacade;
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
@Path("part-offer")
public class PartOfferWebService extends BaseWebService {

    @Inject
    private PartOfferDbFacade partOfferDbFacade;

    @POST
    @Path("count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Long count(final PartOfferFilter filter) {
        return partOfferDbFacade.count(filter, currentUser());
    }

    @POST
    @Path("list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<PartOffer> list(final PartOfferFilter filter) {
        return partOfferDbFacade.list(filter, currentUser());
    }

    @POST
    @Path("save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public PartOffer save(final PartOffer entity) {
        return partOfferDbFacade.save(entity, currentUser());
    }

    @POST
    @Path("save-multiple")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<PartOffer> save(final List<PartOffer> entities) {
        return partOfferDbFacade.save(entities, currentUser());
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public PartOffer load(@PathParam("id") final long id) {
        return partOfferDbFacade.loadOrNotFound(id, String.format("Entity with ID: %d cannot be allocated", id));
    }

}
