package cz.muni.fi.pv217.prociv.alerting.service.services;

import cz.muni.fi.pv217.prociv.alerting.service.data.Alert;
import cz.muni.fi.pv217.prociv.alerting.service.data.Location;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class AlertingService {

    @Transactional
    public void addAlert(Alert alert) {
        alert.active = true;
        alert.persist();
    }

    @Transactional
    public void activateAlert(Long id) {
        setActive(id, true);
    }

    @Transactional
    public void deactivateAlert(Long id) {
        setActive(id, false);
    }

    public Alert getAlert(Long id) {
        Alert alert = Alert.findById(id);
        if (alert == null) {
            throw new IllegalArgumentException("Alert " + id + " does not exist!");
        }

        return alert;
    }

    @Transactional
    public List<Alert> getAlertsByLocation(Location location, boolean onlyActive) {
        Stream<Alert> alertStream = Alert.streamAll();
        if (onlyActive) {
            return alertStream.filter(a -> a.active == true && a.location == location).collect(Collectors.toList());
        }

        return alertStream.filter(a -> a.location == location).collect(Collectors.toList());
    }

    public List<Alert> getAllAlerts(boolean onlyActive) {
        Stream<Alert> alertStream = Alert.streamAll();
        if (onlyActive) {
            return alertStream.filter(a -> a.active == true).collect(Collectors.toList());
        }

        return alertStream.collect(Collectors.toList());
    }

    private void setActive(Long id, boolean active) {
        Alert alert = Alert.findById(id);
        if (alert == null) {
            throw new IllegalArgumentException("Alert " + id + " does not exist!");
        }

        alert.active = active;
        alert.persist();
    }
}
