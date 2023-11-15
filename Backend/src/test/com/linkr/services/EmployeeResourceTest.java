package com.linkr.services;

import com.linkr.models.Employee;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration Tests for Employee Resource.
 */
@ExtendWith(ArquillianExtension.class)
public class EmployeeResourceTest extends ArquillianResourceTestBase {

    private final static Logger LOGGER =
        Logger.getLogger(CredentialsResource.class.getName());

    @AfterEach
    public void unload() {
        if (this.client != null) {
            this.client.close();
        }
    }

    @Test
    public void getAllEmployees() throws MalformedURLException {

        webTarget = this.client.target(
            new URL(this.base, "api/employee").toExternalForm());

        final Response response = webTarget.request()
            .accept(MediaType.APPLICATION_JSON)
            .header(tokenName, validToken)
            .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void getKnownEmployeeByID() throws MalformedURLException {

        webTarget = this.client.target(
            new URL(this.base, "api/employee/111").toExternalForm());

        final Response response = webTarget.request()
            .accept(MediaType.APPLICATION_JSON)
            .header(tokenName, validToken)
            .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void getKnownEmployeeBySupervisor() throws MalformedURLException {
        webTarget = this.client.target(
            new URL(this.base, "api/employee/?supervisor-id=112")
                .toExternalForm());

        final Response response = webTarget.request()
            .accept(MediaType.APPLICATION_JSON)
            .header(tokenName, validToken)
            .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void getKnownEmployeeByApprover() throws MalformedURLException {
        webTarget = this.client.target(
            new URL(this.base, "api/employee/?approver-id=112")
                .toExternalForm());

        final Response response = webTarget.request()
            .accept(MediaType.APPLICATION_JSON)
            .header(tokenName, validToken)
            .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void createEmployeeConflict() throws MalformedURLException {
        webTarget = this.client.target(
            new URL(this.base, "api/employee/?userName=user&password=pass")
                .toExternalForm());

        JsonObject json = Json.createObjectBuilder()
            .add("employeeID", 111)
            .add("firstName", "test")
            .add("lastName", "test")
            .add("flexTime", 0.0)
            .add("hrEmployee", true)
            .add("payRate", "P1")
            .add("vacationDays", 0.0)
            .add("sickDays", 0.0)
            .add("salariedEmployee", true)
            .add("active", true)
            .add("supervisorID", 112)
            .add("approverID", 112).build();


        final Response response = webTarget.request()
            .accept(MediaType.APPLICATION_JSON)
            .header(tokenName, validToken)
            .post(Entity.json(json.toString()));
        
        assertEquals(Response.Status.CONFLICT.getStatusCode(), response.getStatus());
    }

    @Test
    public void updateKnownEmployee() throws MalformedURLException {
        webTarget = this.client.target(
            new URL(this.base, "api/employee").toExternalForm());

        Employee employee = new Employee();

        JsonObject json = Json.createObjectBuilder()
            .add("employeeID", 111)
            .add("firstName", "test")
            .add("lastName", "test")
            .add("active", true)
            .add("hrEmployee", true).build();

        final Response response = webTarget.request()
            .accept(MediaType.APPLICATION_JSON)
            .header(tokenName, validToken)
            .put(Entity.json(json.toString()));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }
}