package cz.fi.muni.pv217.prociv.information.service.data;

import java.time.LocalDate;

public class WeatherInfoCreateDTO {
    public Location location;
    public LocalDate date;
    public float temperatureDay;
    public float temperatureNight;
    public int humidity;
    public SkyStatus skyStatus;
    public int rainChance;
}
