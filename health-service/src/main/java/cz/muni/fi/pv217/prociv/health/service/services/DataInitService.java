package cz.muni.fi.pv217.prociv.health.service.services;

import cz.muni.fi.pv217.prociv.health.service.data.HealthInfo;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class DataInitService {

    @Inject
    private HealthService healthService;

    public void onStart(@Observes StartupEvent ev) {
        HealthInfo authService = new HealthInfo();
        authService.name = "auth-service";
        authService.url = "http://host.docker.internal:8081";
        healthService.addProCivService(authService);

        HealthInfo alertingService = new HealthInfo();
        alertingService.name = "alerting-service";
        alertingService.url = "http://host.docker.internal:8080";
        healthService.addProCivService(alertingService);
    }
}