package data;

import exceptions.SensorException;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import java.util.List;
import java.util.Random;

@Entity
public class Sensor extends PanacheEntity {
    public Long id;
    public Location location;
    public SensorData data;

    public static Sensor findById(Long id) {
        return find("id", id).firstResult();
    }

    public static List<Sensor> findByLocation(Location location) {
        return find("location", location).list();
    }

    public Sensor(Location location) throws SensorException {
        this.location = location;
        int sky = new Random().nextInt(SkyStatus.values().length);
        this.data = new SensorData(
                new Random().nextFloat()*100,
                new Random().nextFloat()*50 - 20,
                new Random().nextFloat()*30,
                new Random().nextFloat()*200,
                new Random().nextFloat()*10000 + 96000,
                SkyStatus.values()[sky]
        );
    }
}
