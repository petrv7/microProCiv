package services;

import data.Location;
import data.Sensor;
import data.SensorData;
import data.SkyStatus;
import exceptions.SensorException;
import org.jboss.logging.Logger;

import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@ApplicationScoped
public class SensorService {
    private static final Logger LOGGER = Logger.getLogger(SensorService.class);

    @Retry(maxRetries = 1)
    @Fallback(fallbackMethod = "defaultResponseList")
    public List<Sensor> listSensors() throws SensorException  {
        maybeFail();
        return Sensor.listAll();
    }

    @Transactional
    public void registerSensor(Sensor sensor) throws SensorException {
        maybeFail();
        sensor.persist();
    }

    @Transactional
    public Sensor getSensor(Long id) {
        return Sensor.findById(id);
    }

    @Transactional
    public List<Sensor> getSensorsByLocation(Location location) {
        return Sensor.findByLocation(location);
    }

    private List<Sensor> defaultResponseList() {
        return new ArrayList<Sensor>();
    }

    private void maybeFail() throws SensorException {
        if (new Random().nextBoolean()) {
            LOGGER.error("FAIL");
            throw new SensorException("Failed to access sensor service");
        }
    }
}
