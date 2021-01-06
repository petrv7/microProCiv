import com.fasterxml.jackson.databind.ObjectMapper;
import data.*;
import exceptions.SensorException;
import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import services.SensorService;
import io.restassured.http.ContentType;

import java.util.ArrayList;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@QuarkusTest
public class SensorResourceTest {

    @InjectMock
    private SensorService sensorService;
    @InjectMock
    private SecurityIdentity user;

    private ObjectMapper mapper = new ObjectMapper();

    private static Sensor sensor;
    private static SensorCreateDTO sensorCreateDTO;
    private static Sensor sensorPrague;
    private static Sensor sensorBrno;
    private static SensorData sensorData;

    @BeforeAll
    public static void init() throws SensorException {
        sensor = new Sensor(Location.KRALOVEHRADECKY);
        sensorBrno = new Sensor(Location.JIHOMORAVSKY);
        sensorPrague = new Sensor(Location.STREDOCESKY);
        int sky = new Random().nextInt(SkyStatus.values().length);
        sensorData = new SensorData(
                new Random().nextFloat()*100,
                new Random().nextFloat()*50 - 20,
                new Random().nextFloat()*30,
                new Random().nextFloat()*200,
                new Random().nextFloat()*10000 + 96000,
                SkyStatus.values()[sky]
        );

        sensorCreateDTO = new SensorCreateDTO();
        sensorCreateDTO.location = Location.KRALOVEHRADECKY;
    }

    @Test
    public void testGetSensorById() {
        when(sensorService.getSensor((long)1)).thenReturn(sensor);

        given().when().get("sensors/1")
                .then()
                .statusCode(200)
                .body(is(getJsonString(sensor)));
    }

    @Test
    public void testRegisterSensor() {
        when(user.hasRole("Admin")).thenReturn(true);

        given().contentType(ContentType.JSON).body(sensorCreateDTO)
                .when().post("sensors/auth/new")
                .then()
                .statusCode(200)
                .body(is("Sensor added successfully"));
    }

    @Test
    public void testGetSensorsByLocation() throws SensorException {
        ArrayList<Sensor> brnoSensors = new ArrayList<>();
        ArrayList<Sensor> pragueSensors = new ArrayList<>();

        brnoSensors.add(sensorBrno);
        pragueSensors.add(sensorPrague);

        when(sensorService.getSensorsByLocation(Location.JIHOMORAVSKY)).thenReturn(brnoSensors);
        when(sensorService.getSensorsByLocation(Location.STREDOCESKY)).thenReturn(pragueSensors);

        given().when().get("sensors/location/JIHOMORAVSKY").then().statusCode(200).body(is(getJsonString(brnoSensors)));
        given().when().get("sensors/location/STREDOCESKY").then().statusCode(200).body(is(getJsonString(pragueSensors)));
    }

    @Test
    public void testGetAllSensors() throws SensorException {
        ArrayList<Sensor> sensors = new ArrayList<>();
        sensors.add(sensor);
        sensors.add(sensorBrno);
        sensors.add(sensorPrague);

        when(sensorService.listSensors()).thenReturn(sensors);
        given().when().get("sensors/all").then().statusCode(200).body(is(getJsonString(sensors)));
    }

    @Test
    public void testGetSensorData() throws SensorException {
        when(sensorService.getSensorData((long)1)).thenReturn(sensorData);

        given().when().get("sensors/1/data")
                .then()
                .statusCode(200);
    }

    private String getJsonString(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (Exception e) {
            return "{}";
        }
    }
}
