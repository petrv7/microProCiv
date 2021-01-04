package cz.muni.fi.pv217.prociv.auth.service;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;

import javax.ws.rs.core.Application;

@OpenAPIDefinition(
        info = @Info(
                title="Authentication API",
                description = "Authentication API for microProCiv system. Provides ability to register a new user and login to receive a JWT token.",
                version = "1.0")
)
public class ApiApplication extends Application {
}
