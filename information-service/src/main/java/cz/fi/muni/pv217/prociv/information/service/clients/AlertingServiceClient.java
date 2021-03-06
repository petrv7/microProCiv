package cz.fi.muni.pv217.prociv.information.service.clients;

import cz.fi.muni.pv217.prociv.information.service.data.Alert;
import cz.fi.muni.pv217.prociv.information.service.data.Location;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@RegisterRestClient(configKey = "alerting-service")
@ApplicationScoped
@Path("/alerts")
public interface AlertingServiceClient {
    @GET
    @Path("/location/{loc}/active")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Find active alerts in a specific location.")
    @APIResponse(responseCode = "200", description = "List of active alerts in a specific location.")
    List<Alert> getActiveAlertsByLocation(@PathParam("loc") Location loc);
}
