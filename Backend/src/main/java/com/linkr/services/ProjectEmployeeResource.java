package com.linkr.services;

import com.linkr.access.EmployeeAccessor;
import com.linkr.access.ProjectAccessor;
import com.linkr.access.ProjectEmployeeAccessor;
import com.linkr.models.*;
import com.linkr.services.filters.Authenticated;
import com.linkr.services.utils.ResourceUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * The type Project employee resource.
 * @author Team Linkr
 * @version 1.0
 */
@Path("/project-Employee")
@Stateless
@Authenticated
public class ProjectEmployeeResource {

    /**
     * Used to access credentials information from database.
     */
    @Inject
    private EmployeeAccessor employeeAccessor;

    /**
     * Used to access credentials information from database.
     */
    @Inject
    private ProjectAccessor projectAccessor;

    /**
     * Used to access credentials information from database.
     */
    @Inject
    private ProjectEmployeeAccessor projectEmployeeAccessor;

    /**
     * Gets project employees by employee id.
     *
     * @param employeeID the employee id
     * @param projectID  the project id
     * @return the project employees by employee id
     */
    @GET
    @Produces("application/json")
    public Response getProjectEmployeesByEmployeeID(
            @QueryParam("employeeID") final Integer employeeID,
            @QueryParam("projectID") final String projectID) {
        List<ProjectEmployee> projectEmployeeList;
        if (employeeID == null) {
            projectEmployeeList = projectEmployeeAccessor.
                    findAllByProjectID(projectID);
            if (projectEmployeeList == null) {
                return Response.status(Response.Status.BAD_REQUEST).
                        entity("Project does not exist").build();
            } else {
                return ResourceUtils.
                        entitiesFoundResponseProvider(projectEmployeeList);
            }
        } else if (projectID == null) {
            projectEmployeeList = projectEmployeeAccessor.
                    findAllByEmployeeID(employeeID);
            if (projectEmployeeList == null) {
                return Response.status(Response.Status.BAD_REQUEST).
                        entity("Employee does not exist").build();
            } else {
                return ResourceUtils.
                        entitiesFoundResponseProvider(projectEmployeeList);
            }
        } else {
            ProjectEmployee projectEmployee = projectEmployeeAccessor.
                    find(employeeID, projectID);
            if (projectEmployee == null) {
                return Response.status(Response.Status.BAD_REQUEST).
                        entity("No such employee in project").build();
            } else {
                return ResourceUtils.
                        entityFoundResponseProvider(projectEmployee);
            }
        }
    }

    /**
     * Create project employee response.
     *
     * @param projectEmployee the project employee
     * @return the response
     */
    @POST
    @Consumes("application/json")
    public Response createProjectEmployee(ProjectEmployee projectEmployee) {

        ProjectEmployee currProjEmployee = projectEmployeeAccessor.
                find(projectEmployee.getEmployee().getEmployeeID(),
                        projectEmployee.getProject().getProjectID());

        if (currProjEmployee != null) {
            return Response.status(Response.Status.CONFLICT).build();
        }

        int employeeID = projectEmployee.getEmployee().getEmployeeID();
        Employee myEmployee = employeeAccessor.find(employeeID);
        if (myEmployee == null) {
            return Response.status(Response.Status.BAD_REQUEST).
                    entity("Employee does not exist").build();
        }
        
        String projectID = projectEmployee.getProject().getProjectID();
        Project myProject = projectAccessor.find(projectID);
        if (myProject == null) {
            return Response.status(Response.Status.BAD_REQUEST).
                    entity("Project does not exist").build();
        }
        
        projectEmployee.setEmployee(myEmployee);
        projectEmployee.setProject(myProject);
        projectEmployeeAccessor.persist(projectEmployee);
        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * Update project employee response.
     *
     * @param projectEmployeeList the project employee list
     * @return the response
     */
    @PUT
    @Consumes("application/json")
    public Response updateProjectEmployee(
            ProjectEmployeeList projectEmployeeList) {

        String projectID = projectEmployeeList.getProjectId();
        Project myProject = projectAccessor.find(projectID);
        if (myProject == null) {
            return Response.status(Response.Status.BAD_REQUEST).
                    entity("Project does not exist").build();
        }

        List<ProjectEmployee> currProjEmployees = projectEmployeeAccessor.
                findAllByProjectID(projectID);

        if (currProjEmployees.size() == 0) {
            for (int i = 0;
                 i < projectEmployeeList.getEmployeeIds().size(); i++) {
                Employee currEmployee = employeeAccessor.
                        find(projectEmployeeList.getEmployeeIds().get(i));
                ProjectEmployee projectEmployee = new ProjectEmployee();
                projectEmployee.setEmployee(currEmployee);
                projectEmployee.setProject(myProject);
                projectEmployeeAccessor.persist(projectEmployee);
            }
            return Response.status(Response.Status.CREATED).build();
        }

        for (ProjectEmployee currProjEmployee : currProjEmployees) {
            projectEmployeeAccessor.remove(currProjEmployee);
        }

        for (int i = 0; i < projectEmployeeList.getEmployeeIds().size(); i++) {
            Integer employeeId = projectEmployeeList.getEmployeeIds().get(i);
            Employee currEmployee = employeeAccessor.find(employeeId);
            ProjectEmployee projectEmployee = new ProjectEmployee();
            projectEmployee.setEmployee(currEmployee);
            projectEmployee.setProject(myProject);
            projectEmployeeAccessor.persist(projectEmployee);
        }
        return Response.status(Response.Status.CREATED).build();
    }
}
