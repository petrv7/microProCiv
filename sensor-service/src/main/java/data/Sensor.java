package data;

import exceptions.SensorException;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import java.util.List;
import java.util.Random;

@Entity
public class Sensor extends PanacheEntity {
    public Sensor(){}

    public Sensor(Location location) {
        this.location = location;
        int sky = new Random().nextInt(SkyStatus.values().length);
    }

    public Location location;

    public static Sensor findById(Long id) {
        return find("id", id).firstResult();
    }

    public static List<Sensor> findByLocation(Location location) {
        return find("location", location).list();
    }


}
