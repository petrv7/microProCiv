package cz.muni.fi.pv217.prociv.alerting.service;

import cz.muni.fi.pv217.prociv.alerting.service.data.Alert;
import cz.muni.fi.pv217.prociv.alerting.service.data.Location;
import cz.muni.fi.pv217.prociv.alerting.service.exceptions.AlertException;
import cz.muni.fi.pv217.prociv.alerting.service.services.AlertingService;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/alerts")
public class AlertingResource {

    @Inject
    private AlertingService alertingService;

    @POST
    @Path("/new")
    @RolesAllowed("Admin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Counted(name = "numberOfNewAlerts", description = "How many new alerts have been added.")
    @Timed(name = "newAlertTimer", description = "A measure of how long it takes to add a new alert.", unit = MetricUnits.MILLISECONDS)
    @Operation(summary = "Add a new alert. Requires admin JWT authorization token.")
    @RequestBody(description = "Alert info")
    @APIResponse(responseCode = "200", description = "Successfully added new alert.")
    @APIResponse(responseCode = "401", description = "Unauthorized access.")
    public String newAlert(Alert alert) throws AlertException {
        try {
            alertingService.addAlert(alert);
            return "Alert successfully added!";
        } catch (Exception e) {
            throw new AlertException(e.getMessage());
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Find alert with specific ID.")
    @APIResponse(responseCode = "200", description = "Alert with given ID found.")
    public Alert getAlert(@PathParam("id") Long id) throws AlertException {
        try {
            return alertingService.getAlert(id);
        } catch (Exception e) {
            throw new AlertException(e.getMessage());
        }
    }

    @GET
    @Path("/location/{loc}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed(name = "getAlertsByLocationTimer", description = "A measure of how long it takes to filter alerts based on location.", unit = MetricUnits.MILLISECONDS)
    @Operation(summary = "Find all alerts in a specific location.")
    @APIResponse(responseCode = "200", description = "List of all alerts in a specific location.")
    public List<Alert> getAlertsByLocation(@PathParam("loc") Location loc) {
        return alertingService.getAlertsByLocation(loc, false);
    }

    @GET
    @Path("/location/{loc}/active")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed(name = "getActiveAlertsByLocationTimer", description = "A measure of how long it takes to filter active alerts based on location.", unit = MetricUnits.MILLISECONDS)
    @Operation(summary = "Find active alerts in a specific location.")
    @APIResponse(responseCode = "200", description = "List of active alerts in a specific location.")
    public List<Alert> getActiveAlertsByLocation(@PathParam("loc") Location loc) {
        return alertingService.getAlertsByLocation(loc, true);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Find all alerts.")
    @APIResponse(responseCode = "200", description = "List of all alerts.")
    public List<Alert> getAllAlerts() {
        return alertingService.getAllAlerts(false);
    }

    @GET
    @Path("/active")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Find active alerts.")
    @APIResponse(responseCode = "200", description = "List of active alerts.")
    public List<Alert> getAllActiveAlerts() {
        return alertingService.getAllAlerts(true);
    }

    @PUT
    @RolesAllowed("Admin")
    @Path("/activate/{id}")
    @Counted(name = "numberOfActivateAlerts", description = "How many times alerts have been activated.")
    @Operation(summary = "Activate alert with specific ID. Requires admin JWT authorization token.")
    @APIResponse(responseCode = "200", description = "Alert successfully activated.")
    @APIResponse(responseCode = "401", description = "Unauthorized access.")
    public String activateAlert(@PathParam("id") Long id) throws AlertException {
        try {
            alertingService.activateAlert(id);
            return "Alert " + id + " activated!";
        } catch (Exception e) {
            throw new AlertException(e.getMessage());
        }
    }

    @PUT
    @RolesAllowed("Admin")
    @Path("/deactivate/{id}")
    @Counted(name = "numberOfDeactivateAlerts", description = "How many times alerts have been deactivated.")
    @Operation(summary = "Deactivate alert with specific ID. Requires admin JWT authorization token.")
    @APIResponse(responseCode = "200", description = "Alert successfully deactivated.")
    @APIResponse(responseCode = "401", description = "Unauthorized access.")
    public String deactivateAlert(@PathParam("id") Long id) throws AlertException {
        try {
            alertingService.deactivateAlert(id);
            return "Alert " + id + " deactivated!";
        } catch (Exception e) {
            throw new AlertException(e.getMessage());
        }
    }
}