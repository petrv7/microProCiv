package cz.muni.fi.pv217.prociv.alerting.service.services;

import cz.muni.fi.pv217.prociv.alerting.service.data.Report;
import cz.muni.fi.pv217.prociv.alerting.service.data.Location;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class ReportingService {

    @Transactional
    public void addReport(Report report) {
        report.persist();
    }

    public Report getReport(Long id) {
        Report report = Report.findById(id);
        if (report == null) {
            throw new IllegalArgumentException("Report " + id + " does not exist!");
        }

        return report;
    }

    @Transactional
    public List<Report> getFilteredReports(Date date, Location location) {
        Stream<Report> reportStream = Report.streamAll();

        if (date != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            reportStream = reportStream.filter(r -> format.format(r.date).equals(format.format(date)));
        }

        if (location != null) {
            reportStream = reportStream.filter(r -> r.location == location);
        }

        return reportStream.collect(Collectors.toList());
    }

    public List<Report> getAllReports() {
        return Report.listAll();
    }
}
