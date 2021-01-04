package cz.fi.muni.pv217.prociv.information.service.clients;

import cz.fi.muni.pv217.prociv.information.service.data.Location;
import cz.fi.muni.pv217.prociv.information.service.data.Sensor;
import cz.fi.muni.pv217.prociv.information.service.data.SensorData;
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

@RegisterRestClient(configKey = "sensor-service")
@ApplicationScoped
@Path("/sensors")
public interface SensorServiceClient {

    @GET
    @Path("/location/{loc}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Finds a sensor by location")
    @APIResponse(responseCode = "200", description = "Sensors by location")
    List<Sensor> getSensorsByLocation(@PathParam("loc") Location location);

    @GET
    @Path("/{id}/data")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Gets sample sensor data")
    @APIResponse(responseCode = "200", description = "sensor data")
    SensorData getSensorData(@PathParam("id") Long id);
}
