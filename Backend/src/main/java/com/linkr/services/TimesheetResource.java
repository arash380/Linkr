package com.linkr.services;


import com.linkr.access.*;
import com.linkr.models.*;
import com.linkr.services.filters.Authenticated;
import com.linkr.services.utils.ResourceUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


/**
 * The type Timesheet resource.
 * @author Team 4911
 * @version 1.0
 */
@Path("/timesheet")
@Stateless
@Authenticated
public class TimesheetResource {

    /**
     * Used to access timesheet information from database.
     */
    @Inject
    private TimesheetAccessor timesheetAccessor;

    /**
     * Used to access employee information from database.
     */
    @Inject
    private EmployeeAccessor employeeAccessor;

    /**
     * Used to access row information from database.
     */
    @Inject
    private TimesheetRowAccessor timesheetRowAccessor;

    /**
     * Used to access WP information from database.
     */
    @Inject
    private WorkPackageAccessor workPackageAccessor;

    /**
     * Used to access rate sheet information from database.
     */
    @Inject
    private RateSheetAccessor rateSheetAccessor;

    /**
     * Gets timesheetby id.
     *
     * @param id the id
     * @return the timesheetby id
     */
    @GET
    @Path("{time-id}")
    @Produces("application/json")
    public Response getTimesheetbyId(@PathParam("time-id") final Integer id) {
        Timesheet timesheet = timesheetAccessor.find(id);
        return ResourceUtils.entityFoundResponseProvider(timesheet);
    }

    /**
     * Gets timesheet.
     *
     * @param endDate the end date
     * @param empID   the emp id
     * @return the timesheet
     */
    @GET
    @Produces("application/json")
    public Response getTimesheet(
        @QueryParam("end-date") final LocalDate endDate,
        @QueryParam("emp-id") final Integer empID) {
        List<Timesheet> timesheets;
        if (empID == null && endDate == null) {
            timesheets = timesheetAccessor.findAll();
            return ResourceUtils.entitiesFoundResponseProvider(timesheets);
        } else if (endDate == null) {
            timesheets = timesheetAccessor.findByEmpId(empID);
            return ResourceUtils.entitiesFoundResponseProvider(timesheets);
        } else if (empID != null) {
            timesheets = timesheetAccessor.
                    findByEmpIdAndEndDateAndVersion(endDate, empID, null);
            return ResourceUtils.entitiesFoundResponseProvider(timesheets);
        } else {
            return Response.status(Response.Status.BAD_REQUEST).
                    entity("emp-id required").build();
        }
    }

    /**
     * Gets approvers timesheets.
     *
     * @param approverID the approver id
     * @param approved   the approved
     * @return the approvers timesheets
     */
    @GET
    @Path("approver/{appr-id}")
    @Produces("application/json")
    public Response getApproversTimesheets(
            @PathParam("appr-id") final Integer approverID,
            @QueryParam("only-approved") final Boolean approved) {
        List<Employee> approvees = employeeAccessor.findByApprover(approverID);
        List<Timesheet> approversTimesheets = new ArrayList<>();
        if (approved != null && approved) {
            for (Employee employee : approvees) {
                List<Timesheet> timesheets = timesheetAccessor.
                        findByEmpId(employee.getEmployeeID()).
                        stream().filter(t -> !t.isManagerApproval()).
                        collect(Collectors.toList());
                approversTimesheets.addAll(timesheets);
            }
        } else {
            for (Employee employee : approvees) {
                approversTimesheets.addAll(timesheetAccessor.
                        findByEmpId(employee.getEmployeeID()));
            }
        }
        return ResourceUtils.entityFoundResponseProvider(approversTimesheets);
    }

    /**
     * Create timesheet response.
     *
     * @param timesheet the timesheet
     * @return the response
     */
    @POST
    @Consumes("application/json")
    public Response createTimesheet(Timesheet timesheet) {

        List<Timesheet> sheets = timesheetAccessor.
                findByEmpIdAndEndDateAndVersion(timesheet.getEndDate(),
                timesheet.getEmployee().getEmployeeID(),
                timesheet.getVersionNumber());

        if (sheets.stream().anyMatch(Timesheet -> true)) {
            return Response.status(Response.Status.CONFLICT).build();
        }

        timesheet.setEmployee(employeeAccessor.
                find(timesheet.getEmployee().getEmployeeID()));
        timesheetAccessor.persist(timesheet);
        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * Update timesheet response.
     *
     * @param timesheet the timesheet
     * @return the response
     */
    @PUT
    @Consumes("application/json")
    public Response updateTimesheet(Timesheet timesheet) {
        Timesheet tsheet = timesheetAccessor.find(
            timesheet.getTimesheetID()
        );
        if (tsheet == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Employee currentEmployee = employeeAccessor.
                find(timesheet.getEmployee().getEmployeeID());
        timesheet.setEmployee(currentEmployee);

        if (timesheet.isManagerApproval()) {
            List<TimesheetRow> rows = timesheetRowAccessor.
                    findAllByTimesheet(timesheet);
            for (TimesheetRow row : rows) {
                float[] hours = row.getHoursInDays();
                float totalHours = 0;
                for (Float hour : hours) {
                    totalHours += hour;
                }
                switch (row.getWorkPackage().getWorkpackageID()) {
                    case "SICK":
                        currentEmployee.setSickDays(currentEmployee.
                                getSickDays() - totalHours);
                        break;
                    case "VACN":
                        currentEmployee.setVacationDays(currentEmployee.
                                getVacationDays() - totalHours);
                        break;
                    case "FLEX":
                        currentEmployee.setFlexTime(currentEmployee.
                                getFlexTime() - totalHours);
                        break;
                    default:
                        WorkPackage currentPackage = workPackageAccessor.
                                find(row.getWorkPackage().getWorkpackageID(),
                                        row.getWorkPackage().
                                                getProject().getProjectID()
                                );
                        int year = currentPackage.
                                getWorkpackageStartDate().getYear();
                        float employeeRate;
                        if (currentEmployee.getPayRate().
                                equalsIgnoreCase("P6")) {
                            employeeRate = rateSheetAccessor.find("P5", year);
                        } else {
                            employeeRate = rateSheetAccessor.
                                    find(currentEmployee.getPayRate().
                                            toUpperCase(Locale.ROOT), year);
                        }

                        if (employeeRate == 0) {
                            Response.status(Response.Status.BAD_REQUEST).
                                    entity("Pay Rate of employee"
                                            + "not found in DB").build();
                        }

                        List<WorkPackage> parentWorkPackages
                                = workPackageAccessor.findParentWorkPackages(
                                        currentPackage.getProject().
                                                getProjectID(), currentPackage.
                                        getWorkpackageID());

                        float totalEmployeeExpense = totalHours * employeeRate;
                        currentPackage.setUnallocatedBudget(
                                        currentPackage.getUnallocatedBudget()
                                                - totalEmployeeExpense);

                        for (WorkPackage wp : parentWorkPackages) {
                            wp.setUnallocatedBudget(wp.getUnallocatedBudget()
                                    - totalEmployeeExpense);
                        }
                }
            }
        }


        timesheetAccessor.merge(timesheet);
        return Response.status(Response.Status.OK).build();
    }

    /**
     * Delete timesheet response.
     *
     * @param id the id
     * @return the response
     */
    @DELETE
    @Path("{id}")
    @Consumes("application/json")
    public Response deleteTimesheet(@PathParam("id") int id) {
        Timesheet tsheet = timesheetAccessor.find(id);

        if (tsheet == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        List<TimesheetRow> childrenRows =
            timesheetRowAccessor.findAllByTimesheet(tsheet);
        for (TimesheetRow row : childrenRows) {
            timesheetRowAccessor.remove(row);
        }
        timesheetAccessor.remove(tsheet);
        return Response.status(Response.Status.OK).build();
    }

}
