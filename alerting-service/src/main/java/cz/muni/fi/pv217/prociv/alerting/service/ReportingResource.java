package cz.muni.fi.pv217.prociv.alerting.service;

import cz.muni.fi.pv217.prociv.alerting.service.data.Report;
import cz.muni.fi.pv217.prociv.alerting.service.data.ReportFilterOptions;
import cz.muni.fi.pv217.prociv.alerting.service.exceptions.AlertException;
import cz.muni.fi.pv217.prociv.alerting.service.services.ReportingService;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

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
    @Counted(name = "numberOfNewReports", description = "How many new reports have been added.")
    @Timed(name = "newReportTimer", description = "A measure of how long it takes to add a new report.", unit = MetricUnits.MILLISECONDS)
    @Operation(summary = "Add a new report. Requires user JWT authorization token.")
    @RequestBody(description = "Report info")
    @APIResponse(responseCode = "200", description = "Successfully added a new report.")
    @APIResponse(responseCode = "401", description = "Unauthorized access.")
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
    @Operation(summary = "Find all reports.")
    @APIResponse(responseCode = "200", description = "List of all reports.")
    public List<Report> getAllReports() {
        return reportingService.getAllReports();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Find report with specific ID.")
    @APIResponse(responseCode = "200", description = "Report with given ID found.")
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
    @Timed(name = "filterReportTimer", description = "A measure of how long it takes to filter reports.", unit = MetricUnits.MILLISECONDS)
    @Operation(summary = "Find all reports with given filter.")
    @RequestBody(description = "Report filter")
    @APIResponse(responseCode = "200", description = "List of all reports with given filter.")
    public List<Report> getFilteredReports(ReportFilterOptions options) {
        return reportingService.getFilteredReports(options.date, options.location);
    }
}
