package services;

import data.Location;
import data.Sensor;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.transaction.Transactional;

@ApplicationScoped
public class DataInitService {
    @Transactional
    public void onStart(@Observes StartupEvent ev) {
        Sensor prahaSensor = new Sensor();
        prahaSensor.name = "Main Prague sensor";
        prahaSensor.location = Location.PRAHA;
        prahaSensor.persist();

        Sensor zlinSensor = new Sensor();
        zlinSensor.name = "Main Zlin sensor";
        zlinSensor.location = Location.ZLINSKY;
        zlinSensor.persist();
    }
}
