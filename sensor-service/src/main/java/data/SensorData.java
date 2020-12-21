package data;

import exceptions.SensorException;

public class SensorData  {
    public float humidity;  // Air humidity in percent
    public float temperature;  // Degrees in Celsius
    public float CO2Level;  // CO2 in air in percent
    public float windSpeed; // in km/h
    public float pressure; // in pascals
    public SkyStatus skyStatus;

    public SensorData(float humidity, float temperature, float CO2Level, float windSpeed, float pressure, SkyStatus skyStatus) throws SensorException {

        this.skyStatus = skyStatus;
        if (humidity < 0 || humidity > 100) {
            throw new SensorException("Wrong data range for humidity");
        }
        this.humidity = humidity;
        this.temperature = temperature;
        this.CO2Level = CO2Level;
        if (CO2Level < 0 || CO2Level > 100) {
            throw new SensorException("Wrong data range for CO2Level");
        }
        this.windSpeed = windSpeed;
        this.pressure = pressure;
    }

}
