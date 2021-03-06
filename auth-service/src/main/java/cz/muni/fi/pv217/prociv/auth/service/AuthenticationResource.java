package cz.muni.fi.pv217.prociv.auth.service;

import cz.muni.fi.pv217.prociv.auth.service.data.AuthData;
import cz.muni.fi.pv217.prociv.auth.service.exceptions.AuthException;
import cz.muni.fi.pv217.prociv.auth.service.services.AuthenticationService;
import cz.muni.fi.pv217.prociv.auth.service.services.TokenService;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;


import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/auth")
public class AuthenticationResource {

    @Inject
    private AuthenticationService authService;

    @Inject
    private TokenService tokenService;

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Counted(name = "numberOfLogins", description = "How many logins have been performed.")
    @Operation(summary = "Login with your credentials.")
    @RequestBody(description = "Credentials")
    @APIResponse(responseCode = "200", description = "Successful login, returns JWT token.")
    @APIResponse(responseCode = "400", description = "Unsuccessful login.")
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
    @Counted(name = "numberOfRegistrations", description = "How many registrations have been performed.")
    @Timed(name = "registrationTimer", description = "A measure of how long it takes to register a new user.", unit = MetricUnits.MILLISECONDS)
    @Operation(summary = "Registration of a new user.")
    @RequestBody(description = "Credentials")
    @APIResponse(responseCode = "200", description = "Successful registration.")
    @APIResponse(responseCode = "400", description = "Unsuccessful registration.")
    public String register(AuthData data) throws AuthException {
        try {
            authService.registerUser(data.username, data.password);
            return "Registration successful!";
        } catch (Exception e) {
            throw new AuthException(e.getMessage());
        }
    }
}