package cz.muni.fi.pv217.prociv.health.service.services;

import cz.muni.fi.pv217.prociv.health.service.data.HealthData;
import cz.muni.fi.pv217.prociv.health.service.data.HealthInfo;
import cz.muni.fi.pv217.prociv.health.service.data.HealthResponse;
import io.quarkus.scheduler.Scheduled;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class HealthService {

    private static final Logger LOG = Logger.getLogger(HealthService.class);

    private List<HealthInfo> proCivServices = new ArrayList<>();

    public List<HealthData> getProCivHealth() {
        List<HealthData> data = new ArrayList<>();

        for (HealthInfo info : proCivServices) {
            data.add(getHealth(info));
        }

        return data;
    }

    private HealthData getHealth(HealthInfo info) {
        HealthData data = new HealthData();
        data.info = info;

        Client client = ClientBuilder.newClient();

        Response response;
        try {
            response = client.target(info.url)
                    .path("/health")
                    .request()
                    .get();
        } catch (Exception e) {
            HealthResponse healthResponse = new HealthResponse();
            healthResponse.status = "NOT RESPONDING";
            data.response = healthResponse;

            client.close();
            return data;
        }

        data.response = response.readEntity(HealthResponse.class);

        response.close();
        client.close();
        return data;
    }

    public void addProCivService(HealthInfo info) {
        proCivServices.add(info);
    }

    @Scheduled(every="10s")
    public void scheduledCheck() {
        List<HealthData> data = getProCivHealth();

        for (HealthData d : data) {
            if (d.response.status.equals("UP")) {
                LOG.info(d.info.name + ": STATUS: " + d.response.status);
            } else {
                LOG.warn(d.info.name + ": STATUS: " + d.response.status);
            }
        }
    }
}
