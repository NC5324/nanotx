package com.tusofia.ndurmush.business.document.web;

import com.tusofia.ndurmush.base.web.BaseWebService;
import com.tusofia.ndurmush.business.document.entity.NtxDocument;
import com.tusofia.ndurmush.business.document.facade.NtxDocumentFacade;
import com.tusofia.ndurmush.business.user.entity.User;
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
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestForm;

import java.io.IOException;
import java.io.InputStream;

@RequestScoped
@Path("/document")
public class NtxDocumentWebService extends BaseWebService {

    @Inject
    private NtxDocumentFacade documentFacade;

    @POST
    @Path("save")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @RolesAllowed({User.ROLE_ADMIN, User.ROLE_BUYER, User.ROLE_SUPPLIER})
    public String upload(@RestForm("file") final InputStream is, @RestForm("name") final String fileName) throws IOException {
        NtxDocument createdFile = documentFacade.createFile(is, fileName, true, currentUser());
        return documentFacade.buildMetaDataString(createdFile);
    }

    @GET
    @Path("{id}")
    public Response download(@PathParam("id") final long documentId) throws IOException {
        return documentFacade.download(documentId);
    }

}
