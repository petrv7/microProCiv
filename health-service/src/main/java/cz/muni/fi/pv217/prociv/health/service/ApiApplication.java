package cz.muni.fi.pv217.prociv.health.service;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import javax.ws.rs.core.Application;

@OpenAPIDefinition(
        info = @Info(
                title="Health API",
                description = "Health API for microProCiv system. Aggregates health status of the system and periodically checks for service availability.",
                version = "1.0")
)
public class ApiApplication extends Application {
}
