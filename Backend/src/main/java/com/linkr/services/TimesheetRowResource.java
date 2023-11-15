package com.linkr.services;

import com.linkr.access.TimesheetAccessor;
import com.linkr.access.TimesheetRowAccessor;
import com.linkr.access.WorkPackageAccessor;
import com.linkr.models.Timesheet;
import com.linkr.models.TimesheetRow;
import com.linkr.models.WorkPackage;
import com.linkr.services.filters.Authenticated;
import com.linkr.services.utils.ResourceUtils;
import com.linkr.services.utils.TimesheetRowTimeConverter;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * The type Timesheet row resource.
 * @author Team Linkr
 * @version 1.0
 */
@Path("/timesheet-row")
@Stateless
@Authenticated
public class TimesheetRowResource {

    /**
     * Used to access row information from database.
     */
    @Inject
    private TimesheetRowAccessor timesheetRowAccessor;

    /**
     * Used to access timesheet information from database.
     */
    @Inject
    private TimesheetAccessor timesheetAccessor;

    /**
     * Used to access WP information from database.
     */
    @Inject
    private WorkPackageAccessor workPackageAccessor;

    /**
     * Gets timesheet rows.
     *
     * @param wpId        the wp id
     * @param projId      the proj id
     * @param timesheetId the timesheet id
     * @return the timesheet rows
     */
    @GET
    @Produces("application/json")
    public Response getTimesheetRows(
        @QueryParam("wp-id") final String wpId,
        @QueryParam("proj-id") final String projId,
        @QueryParam("timesheet-id") final String timesheetId) {

        List<TimesheetRow> timesheets;

        if (timesheetId != null) {
            if (wpId != null && projId != null) {
                WorkPackage workPackage =
                    workPackageAccessor.find(wpId, projId);
                TimesheetRow timesheetRow = timesheetRowAccessor.
                        find(Integer.parseInt(timesheetId), workPackage);
                return ResourceUtils.entityFoundResponseProvider(timesheetRow);
            } else if (wpId == null && projId == null) {
                Timesheet timesheet =
                    timesheetAccessor.find(Integer.parseInt(timesheetId));

                if (timesheet == null) {
                    return Response.status(Response.Status.BAD_REQUEST).
                            entity("Timesheet not found").build();
                }

                timesheets = timesheetRowAccessor.findAllByTimesheet(timesheet);
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity(
                    "Valid combinations (timesheet-id, wp-id, proj-id)"
                            + ", (timesheet-id) or no params").build();
            }
        } else {
            timesheets = timesheetRowAccessor.findAll();
        }

        return ResourceUtils.entityFoundResponseProvider(timesheets);
    }

    /**
     * Create timesheet row response.
     *
     * @param timesheetRow the timesheet row
     * @return the response
     */
    @POST
    @Consumes("application/json")
    public Response createTimesheetRow(TimesheetRow timesheetRow) {
        TimesheetRow row = timesheetRowAccessor.find(
            timesheetRow.getTimesheet().getTimesheetID(),
            timesheetRow.getWorkPackage()
        );
        if (row != null) {
            return Response.status(Response.Status.CONFLICT).build();
        }

        Timesheet timesheet = timesheetAccessor.
                find(timesheetRow.getTimesheet().getTimesheetID());

        if (timesheet == null) {
            return Response.status(Response.Status.BAD_REQUEST).
                    entity("Timesheet not found").build();
        }

        WorkPackage workPackage = workPackageAccessor.
                find(timesheetRow.getWorkPackage().getWorkpackageID(),
                timesheetRow.getProject());

        if (workPackage == null) {
            return Response.status(Response.Status.BAD_REQUEST).
                    entity("Work-package or Project not found").build();
        }
        
        if (!workPackage.isChargable()) {
            return Response.status(Response.Status.BAD_REQUEST).
                    entity("Work Package is not chargable").build();
        }

        timesheetRow.setTimesheet(timesheet);
        timesheetRow.setWorkPackage(workPackage);

        timesheetRowAccessor.persist(timesheetRow);
        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * Update timesheet row response.
     *
     * @param timesheetRow the timesheet row
     * @return the response
     */
    @PUT
    @Consumes("application/json")
    public Response updateTimesheetRow(TimesheetRow timesheetRow) {
        TimesheetRow row = timesheetRowAccessor.find(
            timesheetRow.getTimesheet().getTimesheetID(),
            timesheetRow.getWorkPackage()
        );
        if (row == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        WorkPackage workPackage = workPackageAccessor.
                find(timesheetRow.getWorkPackage().getWorkpackageID(),
                    timesheetRow.getProject());

        if (workPackage == null) {
            return Response.status(Response.Status.BAD_REQUEST).
                    entity("Work-package or Project not found").build();
        }
        
        if (!workPackage.isChargable()) {
            return Response.status(Response.Status.BAD_REQUEST).
                    entity("Work-package not chargeable").build();
        }

        timesheetRow.setTimesheet(timesheetAccessor.
                find(timesheetRow.getTimesheet().getTimesheetID()));

        timesheetRow.setWorkPackage(workPackageAccessor.
                find(timesheetRow.getWorkPackage().getWorkpackageID(),
                timesheetRow.getWorkPackage().
                        getProject().getProjectID()));
        TimesheetRowTimeConverter timesheetRowTimeConverter =
            new TimesheetRowTimeConverter();
        timesheetRow.setHoursWorked(
            timesheetRowTimeConverter.setHours(timesheetRow.getHoursInDays()));
        timesheetRowAccessor.merge(timesheetRow);
        return Response.status(Response.Status.OK).build();
    }

    /**
     * Delete timesheet row response.
     *
     * @param timesheetID   the timesheet id
     * @param workPackageID the work package id
     * @param projectID     the project id
     * @return the response
     */
    @DELETE
    @Consumes("application/json")
    public Response deleteTimesheetRow(
        @QueryParam("timesheet-id") int timesheetID,
        @QueryParam("wp-id") String workPackageID,
        @QueryParam("proj-id") String projectID) {

        WorkPackage workPackage =
            workPackageAccessor.find(workPackageID, projectID);

        if (workPackage == null) {
            return Response.status(Response.Status.NOT_FOUND).
                    entity("Work package not found").build();
        }
        if (!workPackage.isChargable()) {
            return Response.status(Response.Status.BAD_REQUEST).
                    entity("Work-package not chargeable").build();
        }

        TimesheetRow row = timesheetRowAccessor.find(timesheetID, workPackage);

        if (row == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        timesheetRowAccessor.remove(row);
        return Response.status(Response.Status.OK).build();
    }
}
