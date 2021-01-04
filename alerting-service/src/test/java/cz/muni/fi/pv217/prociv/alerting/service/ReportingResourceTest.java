package cz.muni.fi.pv217.prociv.alerting.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pv217.prociv.alerting.service.data.Location;
import cz.muni.fi.pv217.prociv.alerting.service.data.NewReportInfo;
import cz.muni.fi.pv217.prociv.alerting.service.data.Report;
import cz.muni.fi.pv217.prociv.alerting.service.data.ReportFilterOptions;
import cz.muni.fi.pv217.prociv.alerting.service.services.ReportingService;
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
public class ReportingResourceTest {

    @InjectMock
    private ReportingService reportingService;

    @InjectMock
    private SecurityIdentity user;

    private ObjectMapper mapper = new ObjectMapper();

    private Report report;
    private Report otherReport;
    private NewReportInfo reportInfo;
    private List<Report> reports;

    @BeforeEach
    public void setup() {
        when(user.hasRole("User")).thenReturn(true);

        report = new Report();
        report.info = "test report";
        report.location = Location.JIHOCESKY;

        otherReport = new Report();
        otherReport.info = "other report";
        otherReport.location = Location.PRAHA;

        reports = new ArrayList<>();
        reports.add(report);
        reports.add(otherReport);

        reportInfo = new NewReportInfo();
        reportInfo.info = "test report";
        reportInfo.location = Location.JIHOCESKY;
    }

    @Test
    public void newReportTest() {
        given().contentType(ContentType.JSON).body(reportInfo)
                .when().post("/reports/new")
                .then()
                .statusCode(200)
                .body(Matchers.is("Report successfully added!"));

        verify(reportingService, times(1)).addReport(any());
    }

    @Test
    public void newReportUnauthorizedTest() {
        when(user.hasRole("User")).thenReturn(false);

        given().contentType(ContentType.JSON).body(reportInfo)
                .when().post("/reports/new")
                .then()
                .statusCode(403);
    }

    @Test
    public void getReportTest() {
        when(reportingService.getReport(valueOf(1))).thenReturn(report);

        given()
                .when().get("/reports/1")
                .then()
                .statusCode(200)
                .body(is(getJsonString(report)));
    }

    @Test
    public void getReportFailTest() {
        String exception = "Report 1 does not exist!";
        when(reportingService.getReport(valueOf(1))).thenThrow(new IllegalArgumentException(exception));

        given()
                .when().get("/reports/1")
                .then()
                .statusCode(400)
                .body(is(exception));
    }

    @Test
    public void getAllReportsTest() {
        when(reportingService.getAllReports()).thenReturn(reports);
        given()
                .when().get("/reports/")
                .then()
                .statusCode(200)
                .body(is(getJsonString(reports)));
    }

    @Test
    public void getFilteredReportsTest() {
        ReportFilterOptions filter = new ReportFilterOptions();
        filter.location = report.location;

        reports = new ArrayList<>();
        reports.add(report);

        when(reportingService.getFilteredReports(filter.date, filter.location)).thenReturn(reports);

        given().contentType(ContentType.JSON).body(filter)
                .when().get("/reports/filter")
                .then()
                .statusCode(200)
                .body(is(getJsonString(reports)));

        filter.location = otherReport.location;

        reports = new ArrayList<>();
        reports.add(otherReport);

        when(reportingService.getFilteredReports(filter.date, filter.location)).thenReturn(reports);

        given().contentType(ContentType.JSON).body(filter)
                .when().get("/reports/filter")
                .then()
                .statusCode(200)
                .body(is(getJsonString(reports)));
    }

    private String getJsonString(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (Exception e) {
            return "{}";
        }
    }
}