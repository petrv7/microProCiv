package cz.muni.fi.pv217.prociv.auth.service.data;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import javax.persistence.Entity;

@Entity
public class AuthUser extends PanacheEntity {
    public String username;
    public String passwordHash;
    public boolean isAdmin;
}
