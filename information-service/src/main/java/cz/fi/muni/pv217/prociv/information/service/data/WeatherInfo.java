package cz.fi.muni.pv217.prociv.information.service.data;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.panache.common.Sort;

import javax.persistence.Entity;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Entity
public class WeatherInfo extends PanacheEntity {
    public Location location;
    public LocalDate date;
    public float temperatureDay;
    public float temperatureNight;
    public int humidity;
    public SkyStatus skyStatus;
    public int rainChance;

    public static void createWeatherInfo(WeatherInfo weatherInfo) {
        Optional<WeatherInfo> optional = WeatherInfo.find("location = ?1 and date = ?2", weatherInfo.location, weatherInfo.date)
                        .firstResultOptional();
        if(optional.isPresent()) {
            throw new BadRequestException();
            //throw some other exception for duplicate entry instead?
        }
        else {
            WeatherInfo.persist(weatherInfo);
        }
    }

    public static WeatherInfo getWeatherInfo(Location location, LocalDate date) {
        Optional<WeatherInfo> optional = WeatherInfo.find("location = ?1 and date = ?2", location,date).firstResultOptional();
        return optional.orElseThrow(() -> new NotFoundException());
    }

    public static WeatherInfo getLatestWeatherInfo(Location location) {
        List<WeatherInfo> weatherInfos = WeatherInfo.list("location", Sort.by("date").descending(), location);
        if(weatherInfos.isEmpty())
                throw new NotFoundException();
        return weatherInfos.get(0);
    }
}