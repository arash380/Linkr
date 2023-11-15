package com.linkr.services;

import com.linkr.access.CredentialsAccessor;
import com.linkr.access.EmployeeAccessor;
import com.linkr.models.Credentials;
import com.linkr.models.Employee;
import com.linkr.services.filters.Authenticated;
import com.linkr.services.filters.AuthorizationFilter;
import com.linkr.services.filters.Authorized;
import com.linkr.services.filters.AuthorizationLevels;
import com.linkr.services.utils.*;

import javax.ejb.Stateless;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 * The type Credentials resource.
 * @author Team 4911
 * @version 1.0
 */
@Path("/credentials")
public class CredentialsResource {

    /**
     * Admin set username.
     */
    private static final String ADMIN_UNAME =
        ConfigurationSet.getProperty("ADMIN_USERNAME");

    /**
     * Admin set password.
     */
    private static final String ADMIN_PASS =
        ConfigurationSet.getProperty("ADMIN_PASSWORD");

    /**
     * Used to access security context and
     * get request user based on provided JWT.
//     */
    @Context
    private SecurityContext securityContext;

    /**
     * Used to access credential information from database.
     */
    @Inject
    private CredentialsAccessor credentialsAccessor;

    /**
     * Used to access employee information from database.
     */
    @Inject
    private EmployeeAccessor employeeAccessor;

    public CredentialsResource() {};

    /**
     * Gets a single credentials set from the database.
     *
     * @param userName .
     * @param password the password
     * @return 200 and JSON object containing credentials
     */
    @GET
    @Produces("application/json")
    public Response getCredentials(@QueryParam("username") String userName,
                                   @QueryParam("password") String password) {

        boolean isAdmin = (userName.equalsIgnoreCase(ADMIN_UNAME)
                && password.equals(ADMIN_PASS));

        if (isAdmin) {
            AuthorizationLevels level = AuthorizationLevels.ADMIN;

            Credentials credentials = new Credentials();
            credentials.setUserName(ADMIN_UNAME);
            credentials.setPassword(ADMIN_PASS);

            String token =
                PersistenceUtils.createJwt(credentials, level);
            return Response.status(Response.Status.OK).
                    header(ConfigurationSet.getProperty(
                    "TOKEN_NAME"), token).entity(credentials).build();
        }

        Credentials credentials = credentialsAccessor.find(userName);

        if (credentials == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Employee employee =
            employeeAccessor.find(credentials.getEmployeeID());
        if (employee == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        boolean isValid = LoginUtils.validatePassword(credentials.getSalt(),
            credentials.getPassword(), password);


        if (isValid) {
            AuthorizationLevels level =
                employee.isHrEmployee()
                        ? AuthorizationLevels.HR : AuthorizationLevels.OWNER;
            String token =
                PersistenceUtils.createJwt(credentials, level);
            return Response.status(Response.Status.OK).
                    header(ConfigurationSet.getProperty(
                    "TOKEN_NAME"), token).entity(credentials).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    /**
     * Creates account credentials.
     *
     * @param credentials .
     * @return response status 200 or 409 (conflict)
     */
    @POST
    @Authenticated
    @Authorized(levels = AuthorizationLevels.HR)
    @Consumes("application/json")
    public Response createCredentials(Credentials credentials) {
        //@TODO Add try catch when validation is handled in accessor

        Employee emp = employeeAccessor.find(credentials.getEmployeeID());

        //If employee doesn't exist return not found.
        if (emp == null) {
            return Response.status(Response.Status.NOT_FOUND).
                    entity("EmployeeID not found.").build();
        }

        Credentials credsByUserName =
            credentialsAccessor.find(credentials.getUserName());
        Credentials credsByEmployee =
            credentialsAccessor.find(credentials.getEmployeeID());


        //If credentials already do for username or employee, returns conflict.
        if (credsByUserName != null || credsByEmployee != null) {
            return Response.status(Response.Status.CONFLICT).build();
        }

        LoginUtils.setSaltAndHashedPass(credentials, credentials.getPassword());
        credentialsAccessor.persist(credentials);
        return Response.status(Response.Status.OK).build();
    }

    /**
     * Updates account credentials.
     *
     * @param credentials .
     * @return response status 200 or 404
     */
    @PUT
    @Authenticated
    @Consumes("application/json")
    public Response updateCredentials(Credentials credentials) {

        Employee emp = employeeAccessor.find(credentials.getEmployeeID());

        //If employee doesn't exist return not found.
        if (emp == null) {
            return Response.status(Response.Status.NOT_FOUND).
                    entity("EmployeeID not found.").build();
        }

        /*
         * If not admin or owner, return 403 - Forbidden.
         */
        if (!AuthorizationFilter.matchEmpIdOrAdmin(
                credentials.getEmployeeID(), securityContext)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }


        if (credentials.getUserName().
                equalsIgnoreCase(ADMIN_UNAME)
                && !AuthorizationFilter.isAdmin(securityContext)) {
            return Response.status(Response.Status.FORBIDDEN).
                    entity("Restricted Account").build();
        }

        Credentials dbCreds =
            credentialsAccessor.find(credentials.getEmployeeID());

        if (dbCreds == null) {
            return Response.status(Response.Status.NOT_FOUND).
                    entity("No credentials found for employee").build();
        }


        LoginUtils.setSaltAndHashedPass(dbCreds, credentials.getPassword());

        credentialsAccessor.merge(dbCreds);
        return Response.status(Response.Status.OK).build();
    }


    /**
     * Remove credentials response.
     * @param credentials .
     * @return response status 200 or 404
     */
    @DELETE
    @Authenticated
    @Authorized(levels = AuthorizationLevels.HR)
    @Consumes("application/json")
    public Response removeCredentials(Credentials credentials) {
        if (credentials.getUserName().equalsIgnoreCase(ADMIN_UNAME)) {
            return Response.status(Response.Status.FORBIDDEN).
                    entity("Restricted Account").build();
        }

        credentialsAccessor.remove(credentials);
        return Response.status(Response.Status.OK).build();
    }
}
