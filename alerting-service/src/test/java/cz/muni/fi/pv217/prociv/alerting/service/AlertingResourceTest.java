package cz.muni.fi.pv217.prociv.alerting.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pv217.prociv.alerting.service.data.Alert;
import cz.muni.fi.pv217.prociv.alerting.service.data.Location;
import cz.muni.fi.pv217.prociv.alerting.service.services.AlertingService;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static java.lang.Long.valueOf;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;

@QuarkusTest
public class AlertingResourceTest {

    @InjectMock
    private AlertingService alertingService;

    @InjectMock
    private SecurityIdentity user;

    private ObjectMapper mapper = new ObjectMapper();

    private Alert alert;
    private Alert otherAlert;
    private List<Alert> alerts;

    @BeforeEach
    public void setup() {
        when(user.hasRole("Admin")).thenReturn(true);

        alert = new Alert();
        alert.info = "test alert";
        alert.location = Location.JIHOCESKY;

        otherAlert = new Alert();
        otherAlert.info = "other alert";
        otherAlert.location = Location.PRAHA;

        alerts = new ArrayList<>();
        alerts.add(alert);
        alerts.add(otherAlert);
    }

    @Test
    public void newAlertTest() {
        given().contentType(ContentType.JSON).body(alert)
                .when().post("/alerts/new")
                .then()
                .statusCode(200)
                .body(Matchers.is("Alert successfully added!"));

        verify(alertingService, times(1)).addAlert(any());
    }

    @Test
    public void newAlertUnauthorizedTest() {
        when(user.hasRole("Admin")).thenReturn(false);

        given().contentType(ContentType.JSON).body(alert)
                .when().post("/alerts/new")
                .then()
                .statusCode(403);
    }

    @Test
    public void getAlertTest() {
        when(alertingService.getAlert(valueOf(1))).thenReturn(alert);

        given()
                .when().get("/alerts/1")
                .then()
                .statusCode(200)
                .body(is(getJsonString(alert)));
    }

    @Test
    public void getAlertFailTest() {
        String exception = "Alert 1 does not exist!";
        when(alertingService.getAlert(valueOf(1))).thenThrow(new IllegalArgumentException(exception));

        given()
                .when().get("/alerts/1")
                .then()
                .statusCode(400)
                .body(is(exception));
    }

    @Test
    public void getAllAlertsTest() {
        when(alertingService.getAllAlerts(false)).thenReturn(alerts);
        given()
                .when().get("/alerts/")
                .then()
                .statusCode(200)
                .body(is(getJsonString(alerts)));
    }

    @Test
    public void getAllActiveAlertsTest() {
        when(alertingService.getAllAlerts(true)).thenReturn(alerts);

        given()
                .when().get("/alerts/active")
                .then()
                .statusCode(200)
                .body(is(getJsonString(alerts)));
    }

    @Test
    public void getAlertsByLocationTest() {
        alerts = new ArrayList<>();
        alerts.add(alert);

        List<Alert> prahaAlerts = new ArrayList<>();
        prahaAlerts.add(otherAlert);

        when(alertingService.getAlertsByLocation(Location.JIHOCESKY, false)).thenReturn(alerts);
        when(alertingService.getAlertsByLocation(Location.PRAHA, false)).thenReturn(prahaAlerts);

        given()
                .when().get("/alerts/location/JIHOCESKY")
                .then()
                .statusCode(200)
                .body(is(getJsonString(alerts)));

        given()
                .when().get("/alerts/location/PRAHA")
                .then()
                .statusCode(200)
                .body(is(getJsonString(prahaAlerts)));
    }

    @Test
    public void getActiveAlertsByLocationTest() {
        alerts = new ArrayList<>();
        alerts.add(alert);

        List<Alert> prahaAlerts = new ArrayList<>();
        prahaAlerts.add(otherAlert);

        when(alertingService.getAlertsByLocation(Location.JIHOCESKY, true)).thenReturn(alerts);
        when(alertingService.getAlertsByLocation(Location.PRAHA, true)).thenReturn(prahaAlerts);

        given()
                .when().get("/alerts/location/JIHOCESKY/active")
                .then()
                .statusCode(200)
                .body(is(getJsonString(alerts)));

        given()
                .when().get("/alerts/location/PRAHA/active")
                .then()
                .statusCode(200)
                .body(is(getJsonString(prahaAlerts)));
    }

    @Test
    public void activateAlertTest() {
        given()
                .when().put("/alerts/activate/1")
                .then()
                .statusCode(200)
                .body(is("Alert 1 activated!"));

        verify(alertingService, times(1)).activateAlert(valueOf(1));
    }

    @Test
    public void activateAlertUnauthorizedTest() {
        when(user.hasRole("Admin")).thenReturn(false);

        given()
                .when().put("/alerts/activate/1")
                .then()
                .statusCode(403);
    }

    @Test
    public void deactivateAlertTest() {
        given()
                .when().put("/alerts/deactivate/1")
                .then()
                .statusCode(200)
                .body(is("Alert 1 deactivated!"));

        verify(alertingService, times(1)).deactivateAlert(valueOf(1));
    }

    @Test
    public void deactivateAlertUnauthorizedTest() {
        when(user.hasRole("Admin")).thenReturn(false);

        given()
                .when().put("/alerts/deactivate/1")
                .then()
                .statusCode(403);
    }

    private String getJsonString(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (Exception e) {
            return "{}";
        }
    }
}