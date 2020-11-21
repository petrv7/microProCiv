package cz.muni.fi.pv217.prociv.auth.service;

import cz.muni.fi.pv217.prociv.auth.service.data.AuthData;
import cz.muni.fi.pv217.prociv.auth.service.exceptions.AuthException;
import cz.muni.fi.pv217.prociv.auth.service.services.AuthenticationService;
import cz.muni.fi.pv217.prociv.auth.service.services.TokenService;
import io.smallrye.jwt.build.Jwt;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Path("/auth")
public class AuthenticationResource {

    @Inject
    private AuthenticationService authService;

    @Inject
    private TokenService tokenService;

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public String login(AuthData data) throws AuthException {
        try {
            if (!authService.loginUser(data.username, data.password)) {
                throw new AuthException("Invalid password!");
            }

            return tokenService.getToken(data.username, authService.isAdmin(data.username));
        } catch (Exception e) {
            throw new AuthException(e.getMessage());
        }
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public String register(AuthData data) throws AuthException {
        try {
            authService.registerUser(data.username, data.password);
            return "Registration successful!";
        } catch (Exception e) {
            throw new AuthException(e.getMessage());
        }
    }
}