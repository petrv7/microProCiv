package cz.muni.fi.pv217.prociv.auth.service.services;

import cz.muni.fi.pv217.prociv.auth.service.data.AuthUser;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.transaction.Transactional;

@ApplicationScoped
public class DataInitService {
    @Transactional
    public void onStart(@Observes StartupEvent ev) {
        AuthUser user = new AuthUser();
        user.username = "user";
        user.passwordHash = AuthenticationService.createHash("password");
        user.isAdmin = false;
        user.persist();

        AuthUser admin = new AuthUser();
        admin.username = "admin";
        admin.passwordHash = AuthenticationService.createHash("password");
        admin.isAdmin = true;
        admin.persist();
    }
}
