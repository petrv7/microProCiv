package cz.fi.muni.pv217.prociv.information.service;

import cz.fi.muni.pv217.prociv.information.service.clients.AlertingServiceClient;
import cz.fi.muni.pv217.prociv.information.service.clients.SensorServiceClient;
import cz.fi.muni.pv217.prociv.information.service.data.*;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Path("/info")
public class InformationResource {

    @Inject
    @RestClient
    AlertingServiceClient alertingServiceClient;

    @Inject
    @RestClient
    SensorServiceClient sensorServiceClient;

    @GET
    @Path("/get-weather-info/{location}")
    @Produces(MediaType.APPLICATION_JSON)
    @Counted(name = "numberOfWeatherInfoQueries", description = "How many queries on weather information have been called.")
    @Operation(summary = "Get the latest weather info for given location including the active alerts and sensor data.")
    @APIResponse(responseCode = "200", description = "Latest weather info for the given location has been found.")
    @APIResponse(responseCode = "404", description = "There's no weather info for the given location.")
    public CompleteInfo getWeatherInfo(@PathParam("location") Location location) {
        CompleteInfo completeInfo = new CompleteInfo();
        completeInfo.weatherInfo = WeatherInfo.getLatestWeatherInfo(location);
        completeInfo.alerts = alertingServiceClient.getActiveAlertsByLocation(location);
        List<Sensor> sensors = sensorServiceClient.getSensorsByLocation(location);
        List<SensorData> sensorData = new ArrayList<>();
        for (Sensor sensor : sensors) {
            sensorData.add(sensorServiceClient.getSensorData(sensor.id)); //do something about SensorException here?
        }
        completeInfo.sensorData = sensorData;
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
    @APIResponse(responseCode = "400", description = "Bad request, WeatherInfo with given date and location could already exist.")
    @Transactional
    public WeatherInfo addWeatherInfo(WeatherInfo weatherInfo) {
        try {
            WeatherInfo.createWeatherInfo(weatherInfo);
            return weatherInfo;
        }
        catch(PersistenceException e) {
            throw new BadRequestException();
        }
    }

    @GET
    @Path("/get-latest-news")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get the latest news")
    @APIResponse(responseCode = "200", description = "Latest news have been found.")
    @APIResponse(responseCode = "404", description = "There's no news in the database.")
    public News getLatestNews() {
        return News.getLatestNews();
    }

    @POST
    @Path("/add-news")
    @RolesAllowed("Admin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Counted(name = "numberOfNews", description = "How many news have been added.")
    @Timed(name = "AddNewsTimer", description = "A measure of how long it takes to add new news.", unit = MetricUnits.MILLISECONDS)
    @Operation(summary = "Add news. Requires admin JWT authorization token.")
    @RequestBody(description = "News")
    @APIResponse(responseCode = "200", description = "Successfully added news.")
    @APIResponse(responseCode = "401", description = "Unauthorized access.")
    @Transactional
    public News addNews(News news) {
        news.persist(news);
        return news;
    }
}
