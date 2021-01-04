package cz.muni.fi.pv217.prociv.alerting.service.data;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import java.util.Date;

@Entity
public class Report extends PanacheEntity {
    public String username;
    public String info;
    public Location location;
    public Date date;
}
