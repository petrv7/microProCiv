package cz.muni.fi.pv217.prociv.alerting.service;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;

import javax.ws.rs.core.Application;

@OpenAPIDefinition(
        info = @Info(
                title="Alerting API",
                description = "Alerting API for microProCiv system. Provides ability to manage reports and alerts.",
                version = "1.0")
)
public class ApiApplication extends Application {
}
