package cz.fi.muni.pv217.prociv.information.service.services;

import cz.fi.muni.pv217.prociv.information.service.data.Location;
import cz.fi.muni.pv217.prociv.information.service.data.SkyStatus;
import cz.fi.muni.pv217.prociv.information.service.data.WeatherInfo;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.transaction.Transactional;
import java.time.LocalDate;

@ApplicationScoped
public class DataInitService {

    @Transactional
    public void onStartup(@Observes StartupEvent event) {

        WeatherInfo weatherInfo = new WeatherInfo();
        weatherInfo.location = Location.ZLINSKY;
        weatherInfo.date = LocalDate.of(2020,1,3);
        weatherInfo.temperatureDay = -5.5f;
        weatherInfo.temperatureNight = -10;
        weatherInfo.humidity = 20;
        weatherInfo.skyStatus = SkyStatus.CLEAR;
        weatherInfo.rainChance = 1;
        weatherInfo.persist();

        weatherInfo = new WeatherInfo();
        weatherInfo.location = Location.ZLINSKY;
        weatherInfo.date = LocalDate.of(2020,12,15);
        weatherInfo.temperatureDay = 3;
        weatherInfo.temperatureNight = -5;
        weatherInfo.humidity = 81;
        weatherInfo.skyStatus = SkyStatus.CLOUDY;
        weatherInfo.rainChance = 2;
        weatherInfo.persist();

        weatherInfo = new WeatherInfo();
        weatherInfo.location = Location.PRAHA;
        weatherInfo.date = LocalDate.of(2020,12,14);
        weatherInfo.temperatureDay = 4;
        weatherInfo.temperatureNight = -2;
        weatherInfo.humidity = 75;
        weatherInfo.skyStatus = SkyStatus.OVERCAST;
        weatherInfo.rainChance = 10;
        weatherInfo.persist();

        weatherInfo = new WeatherInfo();
        weatherInfo.location = Location.JIHOCESKY;
        weatherInfo.date = LocalDate.of(2020,7,14);
        weatherInfo.temperatureDay = 28;
        weatherInfo.temperatureNight = 18;
        weatherInfo.humidity = 5;
        weatherInfo.skyStatus = SkyStatus.CLEAR;
        weatherInfo.rainChance = 0;
        weatherInfo.persist();

        weatherInfo = new WeatherInfo();
        weatherInfo.location = Location.JIHOMORAVSKY;
        weatherInfo.date = LocalDate.of(2020,3,5);
        weatherInfo.temperatureDay = 12;
        weatherInfo.temperatureNight = 5;
        weatherInfo.humidity = 100;
        weatherInfo.skyStatus = SkyStatus.OVERCAST;
        weatherInfo.rainChance = 80;
        weatherInfo.persist();

        weatherInfo = new WeatherInfo();
        weatherInfo.location = Location.JIHOCESKY;
        weatherInfo.date = LocalDate.of(2020,4,21);
        weatherInfo.temperatureDay = 15;
        weatherInfo.temperatureNight = 12;
        weatherInfo.humidity = 10;
        weatherInfo.skyStatus = SkyStatus.CLOUDY;
        weatherInfo.rainChance = 15;
        weatherInfo.persist();

        weatherInfo = new WeatherInfo();
        weatherInfo.location = Location.KARLOVARSKY;
        weatherInfo.date = LocalDate.of(2020,5,11);
        weatherInfo.temperatureDay = 23;
        weatherInfo.temperatureNight = 15;
        weatherInfo.humidity = 0;
        weatherInfo.skyStatus = SkyStatus.CLEAR;
        weatherInfo.rainChance = 0;
        weatherInfo.persist();

        weatherInfo = new WeatherInfo();
        weatherInfo.location = Location.KRALOVEHRADECKY;
        weatherInfo.date = LocalDate.of(2020,6,28);
        weatherInfo.temperatureDay = 25;
        weatherInfo.temperatureNight = 16;
        weatherInfo.humidity = 70;
        weatherInfo.skyStatus = SkyStatus.OVERCAST;
        weatherInfo.rainChance = 50;
        weatherInfo.persist();

        weatherInfo = new WeatherInfo();
        weatherInfo.location = Location.LIBERECKY;
        weatherInfo.date = LocalDate.of(2020,9,12);
        weatherInfo.temperatureDay = 22;
        weatherInfo.temperatureNight = 13;
        weatherInfo.humidity = 10;
        weatherInfo.skyStatus = SkyStatus.CLEAR;
        weatherInfo.rainChance = 1;
        weatherInfo.persist();

        weatherInfo = new WeatherInfo();
        weatherInfo.location = Location.MORAVSKOSLEZSKY;
        weatherInfo.date = LocalDate.of(2020,11,11);
        weatherInfo.temperatureDay = 8;
        weatherInfo.temperatureNight = 2;
        weatherInfo.humidity = 100;
        weatherInfo.skyStatus = SkyStatus.OVERCAST;
        weatherInfo.rainChance = 90;
        weatherInfo.persist();

        weatherInfo = new WeatherInfo();
        weatherInfo.location = Location.VYSOCINA;
        weatherInfo.date = LocalDate.of(2020,2,25);
        weatherInfo.temperatureDay = -2;
        weatherInfo.temperatureNight = -5;
        weatherInfo.humidity = 50;
        weatherInfo.skyStatus = SkyStatus.OVERCAST;
        weatherInfo.rainChance = 40;
        weatherInfo.persist();

        weatherInfo = new WeatherInfo();
        weatherInfo.location = Location.OLOMOUCKY;
        weatherInfo.date = LocalDate.of(2020,3,23);
        weatherInfo.temperatureDay = 5.5f;
        weatherInfo.temperatureNight = 1;
        weatherInfo.humidity = 30;
        weatherInfo.skyStatus = SkyStatus.CLOUDY;
        weatherInfo.rainChance = 20;
        weatherInfo.persist();

        weatherInfo = new WeatherInfo();
        weatherInfo.location = Location.PARDUBICKY;
        weatherInfo.date = LocalDate.of(2020,5,18);
        weatherInfo.temperatureDay = 18.5f;
        weatherInfo.temperatureNight = 10;
        weatherInfo.humidity = 20;
        weatherInfo.skyStatus = SkyStatus.CLOUDY;
        weatherInfo.rainChance = 10;
        weatherInfo.persist();

        weatherInfo = new WeatherInfo();
        weatherInfo.location = Location.PLZENSKY;
        weatherInfo.date = LocalDate.of(2020,1,29);
        weatherInfo.temperatureDay = -11;
        weatherInfo.temperatureNight = -16.5f;
        weatherInfo.humidity = 0;
        weatherInfo.skyStatus = SkyStatus.CLEAR;
        weatherInfo.rainChance = 1;
        weatherInfo.persist();

        weatherInfo = new WeatherInfo();
        weatherInfo.location = Location.STREDOCESKY;
        weatherInfo.date = LocalDate.of(2020,4,8);
        weatherInfo.temperatureDay = 10;
        weatherInfo.temperatureNight = 8;
        weatherInfo.humidity = 90;
        weatherInfo.skyStatus = SkyStatus.OVERCAST;
        weatherInfo.rainChance = 85;
        weatherInfo.persist();

        weatherInfo = new WeatherInfo();
        weatherInfo.location = Location.USTECKY;
        weatherInfo.date = LocalDate.of(2020,8,5);
        weatherInfo.temperatureDay = 30;
        weatherInfo.temperatureNight = 23;
        weatherInfo.humidity = 10;
        weatherInfo.skyStatus = SkyStatus.CLEAR;
        weatherInfo.rainChance = 2;
        weatherInfo.persist();
    }
}
