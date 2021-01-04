package cz.muni.fi.pv217.prociv.health.service;

import cz.muni.fi.pv217.prociv.health.service.data.HealthData;
import cz.muni.fi.pv217.prociv.health.service.services.HealthService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/health")
public class HealthResource {

    @Inject
    private HealthService healthService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get health status of all microProCiv services.")
    @APIResponse(responseCode = "200", description = "Health status of all microProCiv services is returned.")
    public List<HealthData> getProCivHealth() {
        return healthService.getProCivHealth();
    }
}