package data;

import exceptions.SensorException;

public class SensorData {
    private float humidity;  // Air humidity in percent
    private float temperature;  // Degrees in Celsius
    private float CO2Level;  // CO2 in air in percent
    private float windSpeed; // in km/h
    private float pressure; // in pascals
    private Location location;
    private SkyStatus skyStatus;

    public SensorData(float humidity, float temperature, float CO2Level, float windSpeed, float pressure, Location location, SkyStatus skyStatus) throws SensorException {
        this.location = location;
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

    public float getHumidity() {
        return humidity;
    }

    public float getTemperature() {
        return temperature;
    }

    public float getCO2Level() {
        return CO2Level;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public float getPressure() {
        return pressure;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public SkyStatus getSkyStatus() {
        return skyStatus;
    }

    public void setSkyStatus(SkyStatus skyStatus) {
        this.skyStatus = skyStatus;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public void setCO2Level(float CO2Level) {
        this.CO2Level = CO2Level;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

}
