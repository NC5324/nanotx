package com.tusofia.ndurmush.business.user.web;

import com.tusofia.ndurmush.base.web.BaseWebService;
import com.tusofia.ndurmush.business.user.UserRegisterRequest;
import com.tusofia.ndurmush.business.user.entity.User;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@RequestScoped
@Path("user")
public class UserWebService extends BaseWebService {

    @ConfigProperty(name = "quarkus.http.auth.form.cookie-name")
    private String authCookieName;

    @POST
    @PermitAll
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(final UserRegisterRequest request) {
        Optional<User> existingUser = userDbFacade.findUserByLogin(request.getLogin());
        if (existingUser.isPresent()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Username is already taken!").build();
        }
        User user = new User();
        user.setLogin(request.getLogin());
        user.setPassword(BcryptUtil.bcryptHash(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        return Response.ok(userDbFacade.save(user, null)).build();
    }

    @GET
    @PermitAll
    @Path("current-user")
    @Produces(MediaType.APPLICATION_JSON)
    public User getCurrentUser() {
        return currentUser();
    }

    @GET
    @PermitAll
    @Path("logout")
    public Response logout() {
        NewCookie cookie = new NewCookie.Builder(authCookieName)
                .value("").path("/").domain(null).maxAge(0).expiry(Date.from(Instant.EPOCH)).build();
        return Response.ok().cookie(cookie).build();
    }

}
