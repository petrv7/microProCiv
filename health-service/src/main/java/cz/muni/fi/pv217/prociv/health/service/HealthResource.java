package cz.muni.fi.pv217.prociv.health.service;

import cz.muni.fi.pv217.prociv.health.service.data.HealthData;
import cz.muni.fi.pv217.prociv.health.service.services.HealthService;

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
    public List<HealthData> getProCivHealth() {
        return healthService.getProCivHealth();
    }
}