package cz.muni.fi.pv217.prociv.health.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.fi.pv217.prociv.health.service.data.HealthData;
import cz.muni.fi.pv217.prociv.health.service.data.HealthInfo;
import cz.muni.fi.pv217.prociv.health.service.data.HealthResponse;
import cz.muni.fi.pv217.prociv.health.service.services.HealthService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static io.restassured.RestAssured.given;

@QuarkusTest
public class HealthResourceTest {

    @InjectMock
    public HealthService healthService;

    private static HealthData healthData1;
    private static HealthData healthData2;
    private static List<HealthData> data;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeAll
    public static void setup() {
        data = new ArrayList<>();

        HealthInfo info = new HealthInfo();
        info.url = "testUrl";
        info.name = "testName";

        HealthResponse response = new HealthResponse();
        response.status = "UP";

        healthData1 = new HealthData();
        healthData1.info = info;
        healthData1.response = response;
        data.add(healthData1);

        healthData2 = new HealthData();
        data.add(healthData2);
    }

    @Test
    public void getProCivHealthTest() {
        when(healthService.getProCivHealth()).thenReturn(data);

        given().when().get("/health")
                .then()
                .statusCode(200)
                .body(is(getJsonString(data)));
    }

    private String getJsonString(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (Exception e) {
            return "{}";
        }
    }
}
