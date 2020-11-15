package cz.muni.fi.pv217.prociv.auth.service;

import cz.muni.fi.pv217.prociv.auth.service.data.AuthData;
import cz.muni.fi.pv217.prociv.auth.service.exceptions.AuthException;
import cz.muni.fi.pv217.prociv.auth.service.services.AuthenticationService;
import io.smallrye.jwt.build.Jwt;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.HashSet;

@Path("/auth")
public class AuthenticationResource {

    @Inject
    private AuthenticationService service;

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public String login(AuthData data) throws AuthException {
        try {
            if (!service.loginUser(data.username, data.password)) {
                throw new AuthException("Invalid password!");
            }

            return Jwt.issuer("https://example.com/issuer")
                    .upn(data.username + "@quarkus.io")
                    .groups(new HashSet<>(Arrays.asList("User", "Admin")))
                    .sign();
        } catch (Exception e) {
            throw new AuthException(e.getMessage());
        }
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public String register(AuthData data) throws AuthException {
        try {
            service.registerUser(data.username, data.password);
            return "Registration successful!";
        } catch (Exception e) {
            throw new AuthException(e.getMessage());
        }
    }
}