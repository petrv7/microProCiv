package cz.muni.fi.pv217.prociv.auth.service;

import cz.muni.fi.pv217.prociv.auth.service.data.AuthData;
import cz.muni.fi.pv217.prociv.auth.service.services.AuthenticationService;
import cz.muni.fi.pv217.prociv.auth.service.services.TokenService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@QuarkusTest
public class AuthenticationResourceTest {

    @InjectMock
    private AuthenticationService authService;

    @InjectMock
    private TokenService tokenService;

    private static AuthData correctData;
    private static AuthData failData;

    @BeforeAll
    public static void setup() {
        correctData = new AuthData();
        correctData.username = "testUser";
        correctData.password = "testPassword";

        failData = new AuthData();
        failData.username = "nonexistent";
        failData.password = "testPassword";
    }

    @Test
    public void loginTest() {
        when(authService.loginUser(correctData.username, correctData.password)).thenReturn(true);
        when(authService.isAdmin(correctData.username)).thenReturn(false);
        when(tokenService.getToken(correctData.username, false)).thenReturn("token");

        given().contentType(ContentType.JSON).body(correctData)
                .when().post("/auth/login")
                .then()
                .statusCode(200)
                .body(is("token"));
    }

    @Test
    public void loginFailTest() {
        String exception = "User " + failData.username + " does not exist!";

        when(authService.loginUser(failData.username, failData.password))
                .thenThrow(new IllegalArgumentException(exception));

        given().contentType(ContentType.JSON).body(failData)
                .when().post("/auth/login")
                .then()
                .statusCode(400)
                .body(is(exception));
    }
}