package data;

import exceptions.SensorException;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import java.util.List;
import java.util.Random;

@Entity
public class Sensor extends PanacheEntity {
    public String name;
    public Location location;

    public Sensor() {}

    public Sensor(Location loc) {
        location = loc;
    }

    public static Sensor findById(Long id) {
        return find("id", id).firstResult();
    }

    public static List<Sensor> findByLocation(Location location) {
        return find("location", location).list();
    }
}
