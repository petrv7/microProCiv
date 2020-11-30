package cz.muni.fi.pv217.prociv.alerting.service.data;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;

@Entity
public class Alert extends PanacheEntity {
    public Location location;
    public String info;
    public boolean active;
}
