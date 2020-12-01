package cz.muni.fi.pv217.prociv.alerting.service;

import cz.muni.fi.pv217.prociv.alerting.service.data.Report;
import cz.muni.fi.pv217.prociv.alerting.service.data.ReportFilterOptions;
import cz.muni.fi.pv217.prociv.alerting.service.exceptions.AlertException;
import cz.muni.fi.pv217.prociv.alerting.service.services.ReportingService;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Path("/reports")
public class ReportingResource {

    @Inject
    private ReportingService reportingService;

    @POST
    @Path("/new")
    @RolesAllowed("User")
    @Consumes(MediaType.APPLICATION_JSON)
    public String newReport(Report report, @Context SecurityContext ctx) {
        if (ctx != null && ctx.getUserPrincipal() != null) {
            report.username = ctx.getUserPrincipal().getName();
        } else {
            report.username = "unknown";
        }

        reportingService.addReport(report);
        return "Report successfully added!";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Report> getAllReports() {
        return reportingService.getAllReports();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Report getReport(@PathParam("id") Long id) throws AlertException {
        try {
            return reportingService.getReport(id);
        } catch (Exception e) {
            throw new AlertException(e.getMessage());
        }
    }

    @GET
    @Path("/filter")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Report> getFilteredReports(ReportFilterOptions options) {
        return reportingService.getFilteredReports(options.date, options.location);
    }
}
