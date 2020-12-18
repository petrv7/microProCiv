package services;

import data.Location;
import data.SensorData;
import data.SkyStatus;
import exceptions.SensorException;
import org.jboss.logging.Logger;

import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;

import java.util.Random;


public class SensorService {
    private static final Logger LOGGER = Logger.getLogger(SensorService.class);

    @Retry(maxRetries = 1)
    @Fallback(fallbackMethod = "defaultResponse")
    public SensorData getResponse() throws SensorException {
        maybeFail();
        return generateData();
    }

    private SensorData generateData() throws SensorException {
        int location = new Random().nextInt(Location.values().length);
        int sky = new Random().nextInt(SkyStatus.values().length);
        return new SensorData(
                new Random().nextFloat()*100,
                new Random().nextFloat()*50 - 20,
                new Random().nextFloat()*30,
                new Random().nextFloat()*200,
                new Random().nextFloat()*10000 + 96000,
                Location.values()[location],
                SkyStatus.values()[sky]
        );
    }

    private SensorData defaultResponse() throws SensorException {
        return new SensorData(
                40,
                20,
                (float) 0.1,
                10,
                101325,
                Location.STREDOCESKY,
                SkyStatus.CLEAR
        );
    }

    private void maybeFail() throws SensorException {
        if (new Random().nextBoolean()) {
            LOGGER.error("FAIL");
            throw new SensorException("Sensor unreachable at the moment");
        }
    }
}
