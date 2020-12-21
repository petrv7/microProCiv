import data.Location;
import data.Sensor;
import data.SensorData;
import exceptions.SensorException;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import services.SensorService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

public class SensorResource {
    @Inject
    private SensorService sensorService;

    @POST
    @Path("/new")
    @RolesAllowed("Admin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Counted(name = "numberOfSensorsAdded", description = "Number of sensors added so far")
    @Timed(name = "addSensorTimer", description = "A measure of how long it takes to add a new sensor.", unit = MetricUnits.MILLISECONDS)
    @Operation(summary = "Add a new sensor. Requires admin JWT authorization token.")
    @RequestBody(description = "Sensor setup")
    @APIResponse(responseCode = "200", description = "Successfully added new sensor.")
    @APIResponse(responseCode = "401", description = "Unauthorized access.")
    public String addSensor(Sensor sensor) throws SensorException {
        try {
            sensorService.registerSensor(sensor);
            return "Sensor added successfully";
        } catch (Exception e) {
            throw new SensorException(e.getMessage());
        }
    }

    @GET
    @Path("/id")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed(name = "getSensorByIdTimer", description = "A measure of how long it takes to find the sensor", unit = MetricUnits.MILLISECONDS)
    @Operation(summary = "Finds a sensor by ID")
    @APIResponse(responseCode = "200", description = "Sensor")
    public Sensor getSensorById(Long id) {
        return sensorService.getSensor(id);
    }

    @GET
    @Path("/location/{loc}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed(name = "getSensorByLocationTimer", description = "A measure of how long it takes to filter sensors by location", unit = MetricUnits.MILLISECONDS)
    @Operation(summary = "Finds a sensor by location")
    @APIResponse(responseCode = "200", description = "Sensors by location")
    public List<Sensor> getSensorsByLocation(@PathParam("loc") Location location) {
        return sensorService.getSensorsByLocation(location);
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed(name = "getAllSensorsTimer", description = "A measure of how long it takes to get all sensors", unit = MetricUnits.MILLISECONDS)
    @Operation(summary = "Gets all sensors")
    @APIResponse(responseCode = "200", description = "All sensors")
    public List<Sensor> getAllSensors() throws SensorException {
        return sensorService.listSensors();
    }

    @GET
    @Path("/{id}/data")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed(name = "getSensorData", description = "A measure of how long it takes to get example sensor data", unit = MetricUnits.MILLISECONDS)
    @Operation(summary = "Gets sample sensor data")
    @APIResponse(responseCode = "200", description = "sensor data")
    public SensorData getSensorData(@PathParam("id") Long id) throws SensorException {
        return sensorService.getSensorData(id);
    }
}

