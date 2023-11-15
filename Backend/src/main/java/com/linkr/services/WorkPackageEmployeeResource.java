package com.linkr.services;

import com.linkr.access.EmployeeAccessor;
import com.linkr.access.WorkPackageAccessor;
import com.linkr.access.WorkPackageEmployeeAccessor;
import com.linkr.models.*;
import com.linkr.services.filters.Authenticated;
import com.linkr.services.utils.ResourceUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * The type Work package employee resource.
 *
 * @author Team Linkr
 * @version 1.0
 */
@Path("/workpackage-employee")
@Stateless
@Authenticated
public class WorkPackageEmployeeResource {

    /**
     * Used to access employee information from database.
     */
    @Inject
    private EmployeeAccessor employeeAccessor;

    /**
     * Used to access WP information from database.
     */
    @Inject
    private WorkPackageAccessor workPackageAccessor;

    /**
     * Used to access WPE information from database.
     */
    @Inject
    private WorkPackageEmployeeAccessor workPackageEmployeeAccessor;

    /**
     * Gets work package employees.
     *
     * @param employeeID    the employee id
     * @param workpackageID the workpackage id
     * @param projectID     the project id
     * @param chargable     the chargable
     * @return the work package employees
     */
    @GET
    @Produces("application/json")
    public Response getWorkPackageEmployees(
            @QueryParam("employeeID") final Integer employeeID,
            @QueryParam("workpackageID") final String workpackageID,
            @QueryParam("projectID") final String projectID,
            @QueryParam("chargable") final boolean chargable) {
        List<WorkPackageEmployee> workPackageEmployeeList;
        if (workpackageID == null && projectID == null && employeeID != null) {
            if (chargable) {
                workPackageEmployeeList = workPackageEmployeeAccessor.
                        findAllChargableByEmployeeID(employeeID);
                if (workPackageEmployeeList == null) {
                    return Response.status(Response.Status.BAD_REQUEST).
                            entity("Employee may not exist "
                                    + "or has no chargable workpackages").
                            build();
                } else {
                    return ResourceUtils.
                            entitiesFoundResponseProvider(
                                    workPackageEmployeeList);
                }
            }
            workPackageEmployeeList = workPackageEmployeeAccessor.
                    findAllByEmployeeID(employeeID);
            if (workPackageEmployeeList == null) {
                return Response.status(Response.Status.BAD_REQUEST).
                        entity("Employee does not exist").build();
            } else {
                return ResourceUtils.entitiesFoundResponseProvider(
                        workPackageEmployeeList);
            }
        } else if (workpackageID != null && projectID != null
                && employeeID == null) {
            WorkPackage workPackage = workPackageAccessor.
                    find(workpackageID, projectID);
            workPackageEmployeeList = workPackageEmployeeAccessor.
                    findAllByWorkPackageAndProjectId(workPackage);
            if (workPackageEmployeeList == null) {
                return Response.status(Response.Status.BAD_REQUEST).
                        entity("Workpackage does not exist").build();
            } else {
                return ResourceUtils.entitiesFoundResponseProvider(
                        workPackageEmployeeList);
            }
        } else if (workpackageID != null && projectID != null
                && employeeID != null) {
            WorkPackage workPackage = workPackageAccessor.
                    find(workpackageID, projectID);
            WorkPackageEmployee workPackageEmployee
                    = workPackageEmployeeAccessor.find(employeeID, workPackage);
            if (workPackageEmployee == null) {
                return Response.status(Response.Status.BAD_REQUEST).
                        entity("No such employee in workpackage").build();
            } else {
                return ResourceUtils.entityFoundResponseProvider(
                        workPackageEmployee);
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).
                    entity("Missing search params").build();
        }
    }


    /**
     * Gets all assigned employees.
     *
     * @param projectID the project id
     * @return the all assigned employees
     */
    @GET
    @Path("assigned-employees")
    @Produces("application/json")
    public Response getAllAssignedEmployees(
            @QueryParam("projectID") final String projectID
    ) {
        if (projectID != null) {
            List<WorkPackageEmployee> workPackageEmployees
                    = workPackageEmployeeAccessor.
                    findAllAssignedEmployees(projectID);
            return ResourceUtils.entitiesFoundResponseProvider(workPackageEmployees);
        } else {
            return Response.status(Response.Status.BAD_REQUEST).
                    entity("Missing search params").build();
        }
    }

    /**
     * Create work package employee response.
     *
     * @param workPackageEmployee the work package employee
     * @return the response
     */
    @POST
    @Consumes("application/json")
    public Response createWorkPackageEmployee(
            WorkPackageEmployee workPackageEmployee) {

        WorkPackageEmployee currWPEmp =
                workPackageEmployeeAccessor.find(workPackageEmployee.
                        getEmployee().getEmployeeID(),
                        workPackageEmployee.getWorkPackage());

        if (currWPEmp != null) {
            return Response.status(Response.Status.CONFLICT).build();
        }

        int employeeID = workPackageEmployee.getEmployee().getEmployeeID();
        Employee currEmployee = employeeAccessor.find(employeeID);
        if (currEmployee == null) {
            return Response.status(Response.Status.BAD_REQUEST).
                    entity("Employee does not exist").build();
        }

        String projectID = workPackageEmployee.
                getWorkPackage().getProject().getProjectID();
        String workPackageID = workPackageEmployee.
                getWorkPackage().getWorkpackageID();
        WorkPackage workPackage = workPackageAccessor.
                find(workPackageID, projectID);
        if (workPackage == null) {
            return Response.status(Response.Status.BAD_REQUEST).
                    entity("Workpackage does not exist").build();
        }

        workPackageEmployee.setEmployee(currEmployee);
        workPackageEmployee.setWorkPackage(workPackage);
        workPackageEmployeeAccessor.persist(workPackageEmployee);
        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * Update work package employee response.
     *
     * @param workPackageEmployeeList the work package employee list
     * @return the response
     */
    @PUT
    @Consumes("application/json")
    public Response updateWorkPackageEmployee(
            WorkPackageEmployeeList workPackageEmployeeList) {

        String workPackageId = workPackageEmployeeList.getWorkPackageId();
        String projectID = workPackageEmployeeList.getProjectId();
        WorkPackage workPackage = workPackageAccessor.
                find(workPackageId, projectID);
        if (workPackage == null) {
            return Response.status(Response.Status.BAD_REQUEST).
                    entity("Workpackage does not exist").build();
        }

        List<WorkPackageEmployee> currWPEmployees =
                workPackageEmployeeAccessor.
                        findAllByWorkPackageAndProjectId(workPackage);

        if (currWPEmployees.size() == 0) {
            for (int i = 0;
                 i < workPackageEmployeeList.getEmployeeIds().size(); i++) {
                Employee currEmployee = employeeAccessor.
                        find(workPackageEmployeeList.getEmployeeIds().get(i));
                WorkPackageEmployee workPackageEmployee
                        = new WorkPackageEmployee();
                workPackageEmployee.setWorkPackage(workPackage);
                workPackageEmployee.setEmployee(currEmployee);
                workPackageEmployeeAccessor.persist(workPackageEmployee);
            }
            return Response.status(Response.Status.CREATED).build();
        }

        for (WorkPackageEmployee currWorkPackageEmployee : currWPEmployees) {
            workPackageEmployeeAccessor.remove(currWorkPackageEmployee);
        }

        for (int i = 0;
             i < workPackageEmployeeList.getEmployeeIds().size(); i++) {
            Employee currEmployee = employeeAccessor.
                    find(workPackageEmployeeList.getEmployeeIds().get(i));
            WorkPackageEmployee workPackageEmployee = new WorkPackageEmployee();
            workPackageEmployee.setWorkPackage(workPackage);
            workPackageEmployee.setEmployee(currEmployee);
            workPackageEmployeeAccessor.persist(workPackageEmployee);
        }
        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * Delete work package employee response.
     *
     * @param employeeID    the employee id
     * @param workpackageID the workpackage id
     * @param projectID     the project id
     * @return the response
     */
    @DELETE
    @Consumes("application/json")
    public Response deleteWorkPackageEmployee(
            @QueryParam("employeeID") final Integer employeeID,
            @QueryParam("workpackageID") final String workpackageID,
            @QueryParam("projectID") final String projectID) {
        if (employeeID == null || workpackageID == null || projectID == null) {
            WorkPackage workPackage = workPackageAccessor.
                    find(workpackageID, projectID);
            if (workPackage == null) {
                return Response.status(Response.Status.NOT_FOUND).
                        entity("Work package not found").build();
            }
            Employee employee = employeeAccessor.find(employeeID);
            if (employee == null) {
                return Response.status(Response.Status.NOT_FOUND).
                        entity("Employee not found").build();
            }

            WorkPackageEmployee workPackageEmployee =
                    workPackageEmployeeAccessor.find(employeeID, workPackage);

            if (workPackageEmployee == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

            workPackageEmployeeAccessor.remove(workPackageEmployee);
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).
                    entity("Missing params").build();
        }
    }
}
