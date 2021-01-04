package cz.fi.muni.pv217.prociv.information.service;

import cz.fi.muni.pv217.prociv.information.service.clients.AlertingServiceClient;
import cz.fi.muni.pv217.prociv.information.service.clients.SensorServiceClient;
import cz.fi.muni.pv217.prociv.information.service.data.*;
import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;

import javax.ws.rs.NotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.*;

@QuarkusTest
public class InformationResourceTest {

    @InjectMock
    private SecurityIdentity user;

   @InjectMock
   @RestClient
   private AlertingServiceClient alertingService;

    @InjectMock
    @RestClient
    private SensorServiceClient sensorService;

    @BeforeEach
    public void setup() {
        when(user.hasRole("Admin")).thenReturn(true);
    }

    @Test
    public void getWeatherInfoTest() {
        PanacheMock.mock(WeatherInfo.class);

        WeatherInfo testWeatherInfo = new WeatherInfo();
        testWeatherInfo.location = Location.ZLINSKY;
        Mockito.when(WeatherInfo.getLatestWeatherInfo(Location.ZLINSKY)).thenReturn(testWeatherInfo);

        List<Alert> testAlerts = new ArrayList<Alert>();
        testAlerts.add(new Alert());
        when(alertingService.getActiveAlertsByLocation(Location.ZLINSKY)).thenReturn(testAlerts);

        Sensor testSensor1 = new Sensor();
        testSensor1.id = 1L;
        Sensor testSensor2 = new Sensor();
        testSensor2.id = 2L;
        List<Sensor> testSensors = new ArrayList<>();
        testSensors.add(testSensor1);
        testSensors.add(testSensor2);
        when(sensorService.getSensorsByLocation(Location.ZLINSKY)).thenReturn(testSensors);

        SensorData testSensorData1 = new SensorData();
        testSensorData1.CO2Level = 10;
        testSensorData1.humidity = 70;
        SensorData testSensorData2 = new SensorData();
        testSensorData2.CO2Level = 5;
        testSensorData2.humidity = 35;
        when(sensorService.getSensorData(1L)).thenReturn(testSensorData1);
        when(sensorService.getSensorData(2L)).thenReturn(testSensorData2);

        CompleteInfo returnedCompleteInfo = given()
                .when()
                .get("/info/get-weather-info/ZLINSKY")
                .then()
                .statusCode(200)
                .extract().as(CompleteInfo.class);
        Assertions.assertTrue(new ReflectionEquals(testWeatherInfo).matches(returnedCompleteInfo.weatherInfo));
        Assertions.assertTrue(returnedCompleteInfo.alerts.size() == 1);
        Assertions.assertTrue(returnedCompleteInfo.sensorData.size() == 2);
        Assertions.assertTrue(new ReflectionEquals(testSensorData1).matches(returnedCompleteInfo.sensorData.get(0)));
        Assertions.assertTrue(new ReflectionEquals(testSensorData2).matches(returnedCompleteInfo.sensorData.get(1)));
        PanacheMock.verify(WeatherInfo.class, times(1)).getLatestWeatherInfo(Location.ZLINSKY);
        verify(alertingService, times(1)).getActiveAlertsByLocation(Location.ZLINSKY);
        verify(sensorService, times(1)).getSensorsByLocation(Location.ZLINSKY);
        verify(sensorService, times(2)).getSensorData(any());
    }

    @Test
    public void getWeatherInfoNotFoundTest() {
        PanacheMock.mock(WeatherInfo.class);
        Mockito.when(WeatherInfo.getLatestWeatherInfo(Location.ZLINSKY)).thenThrow(new NotFoundException());

        given()
                .when()
                .get("/info/get-weather-info/ZLINSKY")
                .then()
                .statusCode(404);
        PanacheMock.verify(WeatherInfo.class, times(1)).getLatestWeatherInfo(any());
    }

    @Test
    public void getWeatherInfoSpecificDateTest() {
        PanacheMock.mock(WeatherInfo.class);
        WeatherInfo testWeatherInfo = new WeatherInfo();
        testWeatherInfo.location = Location.PRAHA;

        Mockito.when(WeatherInfo.getWeatherInfo(Location.PRAHA,LocalDate.of(2020,5,12)))
                .thenReturn(testWeatherInfo);

        WeatherInfo returnedWeatherInfo = given()
                .when()
                .get("/info/get-weather-info/PRAHA/2020-05-12")
                .then()
                .statusCode(200)
                .extract().as(WeatherInfo.class);
        Assertions.assertTrue(new ReflectionEquals(testWeatherInfo).matches(returnedWeatherInfo));
        PanacheMock.verify(WeatherInfo.class, times(1)).getWeatherInfo(any(),any());
    }

    @Test
    public void getWeatherInfoSpecificDateNotFoundTest() {
        PanacheMock.mock(WeatherInfo.class);
        Mockito.when(WeatherInfo.getWeatherInfo(any(),any())).thenThrow(new NotFoundException());

        given()
                .when()
                .get("/info/get-weather-info/PRAHA/2020-05-12")
                .then()
                .statusCode(404);
        PanacheMock.verify(WeatherInfo.class, times(1)).getWeatherInfo(any(),any());
    }

    @Test
    public void addWeatherInfoTest() {
        WeatherInfo testWeatherInfo = new WeatherInfo();
        PanacheMock.mock(WeatherInfo.class);

        given().contentType(ContentType.JSON).body(testWeatherInfo)
                .when().post("/info/add-weather-info")
                .then()
                .statusCode(200);
        PanacheMock.verify(WeatherInfo.class,times(1)).createWeatherInfo(any());
    }

    @Test
    public void addWeatherInfoUnauthorizedTest() {
        WeatherInfo testWeatherInfo = new WeatherInfo();
        PanacheMock.mock(WeatherInfo.class);
        when(user.hasRole("Admin")).thenReturn(false);

        given().contentType(ContentType.JSON).body(testWeatherInfo)
                .when().post("/info/add-weather-info")
                .then()
                .statusCode(403);
        PanacheMock.verify(WeatherInfo.class,times(0)).createWeatherInfo(any());
    }


    @Test
    public void getLatestNewsTest() {
        PanacheMock.mock(News.class);
        News testNews = new News();
        testNews.news = "some string";
        testNews.date = LocalDate.of(2021,1,2);

        Mockito.when(News.getLatestNews()).thenReturn(testNews);

        News returnedNews = given()
                .when()
                .get("/info/get-latest-news")
                .then()
                .statusCode(200)
                .extract().as(News.class);
        Assertions.assertTrue(new ReflectionEquals(testNews).matches(returnedNews));
        PanacheMock.verify(News.class, times(1)).getLatestNews();
    }

    @Test
    public void getLatestNewsNotFoundTest() {
        PanacheMock.mock(News.class);
        Mockito.when(News.getLatestNews()).thenThrow(new NotFoundException());

        given()
                .when().get("/info/get-latest-news")
                .then()
                .statusCode(404);
        PanacheMock.verify(News.class, times(1)).getLatestNews();
    }

    @Test
    public void addNewsTest() {
        PanacheMock.mock(News.class);
        News testNews = new News();
        given().contentType(ContentType.JSON).body(testNews)
                .when().post("/info/add-news")
                .then()
                .statusCode(200);
    }

    @Test
    public void addNewsUnauthorizedTest() {
        when(user.hasRole("Admin")).thenReturn(false);
        PanacheMock.mock(News.class);
        News testNews = new News();
        given().contentType(ContentType.JSON).body(testNews)
                .when().post("/info/add-news")
                .then()
                .statusCode(403);
        PanacheMock.verify(News.class, times(0)).persist();
    }
}