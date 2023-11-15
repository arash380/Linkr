package com.linkr.services;

import com.linkr.access.CredentialsAccessor;
import com.linkr.access.EmployeeAccessor;
import com.linkr.models.Credentials;
import com.linkr.models.Employee;
import com.linkr.services.filters.Authenticated;
import com.linkr.services.filters.AuthorizationLevels;
import com.linkr.services.filters.Authorized;
import com.linkr.services.utils.LoginUtils;
import com.linkr.services.utils.ResourceUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * The type Employee resource.
 *
 * @author Team 4911
 * @version 1.0
 */
@Path("/employee")
@Stateless
@Authenticated
public class EmployeeResource {

    /**
     * Used to access employee information from database.
     */
    @Inject
    private EmployeeAccessor employeeAccessor;

    /**
     * Used to access credentials information from database.
     */
    @Inject
    private CredentialsAccessor credentialsAccessor;

    /**
     * Gets a single employee from the database.
     *
     * @param id .
     * @return 200 and JSON object containing employee
     */
    @GET
    @Path("{emp-id}")
    @Produces("application/json")
    public Response getEmployee(@PathParam("emp-id") int id) {
        Employee employee = employeeAccessor.find(id);
        return ResourceUtils.entityFoundResponseProvider(employee);
    }

    /**
     * Gets all employees from the database.
     *
     * @param supervisorID if provided, method will
     *                     get employees assigned to supervisor.
     * @param approverID   get employees by approver.
     * @return 200 and JSON object containing employee
     */
    @GET
    @Produces("application/json")
    public Response getEmployees(
        @QueryParam("supervisor-id") String supervisorID,
        @QueryParam("approver-id") String approverID) {

        if (supervisorID != null && approverID != null) {
            return Response.status(Response.Status.BAD_REQUEST).
                entity("Only 1 param allowed").build();
        }

        if (supervisorID != null && !supervisorID.isEmpty()) {
            List<Employee> employee = employeeAccessor.
                findBySupervisor(Integer.parseInt(supervisorID));
            return ResourceUtils.entitiesFoundResponseProvider(employee);
        }

        if (approverID != null && !approverID.isEmpty()) {
            List<Employee> employee = employeeAccessor.
                findByApprover(Integer.parseInt(approverID));
            return ResourceUtils.entitiesFoundResponseProvider(employee);
        }

        List<Employee> employees = employeeAccessor.findAll();
        return ResourceUtils.entitiesFoundResponseProvider(employees);
    }

    /**
     * Creates employee.
     *
     * @param employee .
     * @param userName the user name
     * @param password the password
     * @return response status 200 or 409 (conflict)
     */
    @POST
    @Authorized(levels = AuthorizationLevels.HR)
    @Consumes("application/json")
    public Response createEmployee(
        Employee employee,
        @QueryParam("userName") String userName,
        @QueryParam("password") String password) {

        Employee emp = employeeAccessor.find(employee.getEmployeeID());
        if (emp != null) {
            return Response.status(Response.Status.CONFLICT).build();
        }

        try {
            validateApproverAndSupervisor(employee);
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.NOT_FOUND).
                entity(ex.getMessage()).build();
        }

        employeeAccessor.persist(employee);
        if (!createCredentials(userName, password, employee.getEmployeeID())) {
            employeeAccessor.remove(employee);
            return Response.status(Response.Status.CONFLICT)
                .entity("Add default 'userName' and 'password' query parameters")
                .build();
        } else {
            return Response.status(Response.Status.CREATED).build();
        }
    }

    private boolean createCredentials(String userName,
                                      String password, int empId) {
        if (userName == null || password == null) {
            return false;
        }

        Employee emp = employeeAccessor.find(empId);

        //If employee doesn't exist return not found.
        if (emp == null) {
            return false;
        }

        Credentials credsByUserName =
            credentialsAccessor.find(userName);
        Credentials credsByEmployee =
            credentialsAccessor.find(empId);


        //If credentials already do for username or employee, returns conflict.
        if (credsByUserName != null || credsByEmployee != null) {
            return false;
        }

        Credentials credentials = new Credentials();
        credentials.setEmployeeID(empId);
        credentials.setPassword(password);
        credentials.setUserName(userName);

        LoginUtils.setSaltAndHashedPass(credentials, credentials.getPassword());
        credentialsAccessor.persist(credentials);
        return true;
    }

    /**
     * Updates account employee.
     *
     * @param employee .
     * @return response status 200 or 404
     */
    @PUT
    @Authorized(levels = AuthorizationLevels.HR)
    @Consumes("application/json")
    public Response updateEmployee(Employee employee) {
        Employee emp = employeeAccessor.find(employee.getEmployeeID());
        if (emp == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        try {
            validateApproverAndSupervisor(employee);
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.NOT_FOUND).
                entity(ex.getMessage()).build();
        }

        employeeAccessor.merge(employee);
        return Response.status(Response.Status.OK).build();
    }

    /**
     * Validates approver and supervisor exist in db.
     *
     * @param employee .
     */
    private void validateApproverAndSupervisor(Employee employee)
        throws IllegalArgumentException {

        Employee supervisor = employee.getSupervisor();
        Employee approver = employee.getApprover();

        if (supervisor != null) {
            Employee existingSupervisor =
                employeeAccessor.find(supervisor.getEmployeeID());
            if (existingSupervisor == null) {
                throw new IllegalArgumentException(
                    "SupervisorID must match existing employee.");
            }
        }

        if (approver != null) {
            Employee existingApprover =
                employeeAccessor.find(approver.getEmployeeID());
            if (existingApprover == null) {
                throw new IllegalArgumentException(
                    "SupervisorID must match existing employee.");
            }
        }
    }

}


