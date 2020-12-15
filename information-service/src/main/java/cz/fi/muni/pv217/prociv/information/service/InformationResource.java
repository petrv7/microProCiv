package cz.fi.muni.pv217.prociv.information.service;

import cz.fi.muni.pv217.prociv.information.service.clients.AlertingServiceClient;
import cz.fi.muni.pv217.prociv.information.service.data.CompleteInfo;
import cz.fi.muni.pv217.prociv.information.service.data.Location;
import cz.fi.muni.pv217.prociv.information.service.data.WeatherInfo;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.DateTimeException;
import java.time.LocalDate;

@Path("/info")
public class InformationResource {

    @Inject
    @RestClient
    AlertingServiceClient AlertingServiceClient;

    @GET
    @Path("/get-weather-info/{location}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get the latest weather info for given location including the active alerts and sensor data.")
    @APIResponse(responseCode = "200", description = "Latest weather info for the given location has been found.")
    @APIResponse(responseCode = "404", description = "There's no weather info for the given location.")
    public CompleteInfo getWeatherInfo(@PathParam("location") Location location) {
        CompleteInfo completeInfo = new CompleteInfo();
        completeInfo.weatherInfo = WeatherInfo.getLatestWeatherInfo(location);
        completeInfo.alerts = AlertingServiceClient.getActiveAlertsByLocation(location);
        //TODO: add sensor data, decide what to do when other services are unavailable
        return completeInfo;
    }

    @GET
    @Path("/get-weather-info/{location}/{year}-{month}-{day}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get the weather info for given location and date.")
    @APIResponse(responseCode = "200", description = "Weather info with given specifications has been found.")
    @APIResponse(responseCode = "404", description = "Weather info with given specifications hasn't been found.")
    public WeatherInfo getWeatherInfo(@PathParam("location") Location location,
                                      @PathParam("year") int year,
                                      @PathParam("month") int month,
                                      @PathParam("day") int day) {
        try {
            LocalDate date = LocalDate.of(year,month,day);
            return WeatherInfo.getWeatherInfo(location,date);
        }
        catch(DateTimeException e) {
            throw new NotFoundException();
        }
    }

    @POST
    @Path("/add-weather-info")
    @RolesAllowed("Admin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Counted(name = "numberOfNewWeatherInfos", description = "How many new weather infos have been added.")
    @Timed(name = "newWeatherInfoTimer", description = "A measure of how long it takes to add a new weather info.", unit = MetricUnits.MILLISECONDS)
    @Operation(summary = "Add a new weather info. Requires admin JWT authorization token.")
    @RequestBody(description = "Weather info")
    @APIResponse(responseCode = "200", description = "Successfully added a new weather info.")
    @APIResponse(responseCode = "401", description = "Unauthorized access.")
    @Transactional
    public WeatherInfo addWeatherInfo(WeatherInfo weatherInfo) {
        WeatherInfo.createWeatherInfo(weatherInfo);
        return weatherInfo;
    }

    //TODO: add getting and adding news
}
