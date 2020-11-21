package cz.muni.fi.pv217.prociv.auth.service;

import cz.muni.fi.pv217.prociv.auth.service.data.AuthData;
import cz.muni.fi.pv217.prociv.auth.service.services.AuthenticationService;
import cz.muni.fi.pv217.prociv.auth.service.services.TokenService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
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

    private AuthData data;

    @BeforeEach
    public void setup() {
        data = new AuthData();
        data.username = "testUser";
        data.password = "testPassword";
        when(authService.loginUser(data.username, data.password)).thenReturn(true);
        when(authService.isAdmin(data.username)).thenReturn(false);
        when(tokenService.getToken(data.username, false)).thenReturn("token");
    }

    @Test
    public void loginTest() {
        given().contentType(ContentType.JSON).body(data)
                .when().post("/auth/login")
                .then()
                .statusCode(200)
                .body(is("token"));
    }
}