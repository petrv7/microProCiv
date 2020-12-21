import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;

import javax.ws.rs.core.Application;

@OpenAPIDefinition(
        info = @Info(
                title="Sensor API",
                description = "Sensor API for microProCiv system. Provides ways to add sensors and get their data.",
                version = "1.0")
)
public class ApiApplication extends Application {
}
