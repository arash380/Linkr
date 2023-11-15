package com.linkr.services;


import com.linkr.access.ProjectAccessor;
import com.linkr.access.ProjectWorkPackageEstimateAccessor;
import com.linkr.access.WorkPackageAccessor;
import com.linkr.models.Project;
import com.linkr.models.ProjectWorkPackageEstimate;
import com.linkr.models.WorkPackage;
import com.linkr.services.filters.Authenticated;
import com.linkr.services.filters.AuthorizationFilter;
import com.linkr.services.utils.ResourceUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import java.time.LocalDate;
import java.util.List;

/**
 * The type Project work package estimate resource.
 * @author Team Linkr
 * @version 1.0
 */
@Path("/project-workpackage-estimate")
@Stateless
@Authenticated
public class ProjectWorkPackageEstimateResource {
    /**
     * Used to access security context and get
     * request user based on provided JWT.
     */
    @Context
    private SecurityContext securityContext;

    /**
     * Used to access PE information from database.
     */
    @Inject
    private ProjectWorkPackageEstimateAccessor
            projectWorkPackageEstimateAccessor;

    /**
     * Used to access work package information from database.
     */
    @Inject
    private WorkPackageAccessor workPackageAccessor;

    /**
     * Used to access project information from database.
     */
    @Inject
    private ProjectAccessor projectAccessor;

    /**
     * Gets project work package estimate.
     *
     * @param id            the id
     * @param workpackageID the workpackage id
     * @param projectID     the project id
     * @return the project work package estimate
     */
    @GET
    @Produces("application/json")
    public Response getProjectWorkPackageEstimate(
            @QueryParam("workpackageID") final String workpackageID,
            @QueryParam("projectID") final String projectID) {
        
        WorkPackage workPackage = workPackageAccessor.
                find(workpackageID, projectID);
        if (workPackage == null) {
            return Response.status(Response.Status.NOT_FOUND).
                    entity("Work package not found").build();
        }
        
        if (workpackageID != null && projectID != null) {
            ProjectWorkPackageEstimate projectWorkPackageEstimate
                    = projectWorkPackageEstimateAccessor.
                    findAllByWorkpackageId(workPackage);
            if (projectWorkPackageEstimate == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return ResourceUtils.
                    entityFoundResponseProvider(projectWorkPackageEstimate);
        } else {
            return Response.status(Response.Status.BAD_REQUEST).
                    entity("Missing search params").build();
        }
    }

    /**
     * Create project work package estimate response.
     *
     * @param projectWorkPackageEstimate the project work package estimate
     * @return the response
     */
    @POST
    @Consumes("application/json")
    public Response createProjectWorkPackageEstimate(
            ProjectWorkPackageEstimate projectWorkPackageEstimate) {

        ProjectWorkPackageEstimate currentProjectWorkPackageEstimate =
                projectWorkPackageEstimateAccessor.
                        findAllByWorkpackageId(
                                projectWorkPackageEstimate.getWorkPackage());

        if (currentProjectWorkPackageEstimate != null) {
            return Response.status(Response.Status.CONFLICT).build();
        }

        String projectID = projectWorkPackageEstimate.
                getWorkPackage().getProject().getProjectID();
        Project currProject = projectAccessor.find(projectID);
        if (currProject == null) {
            return Response.status(Response.Status.BAD_REQUEST).
                    entity("Project does not exist").build();
        }
        
        /*
         * If not admin or owner, return 403 - forbidden.
         */
        if (!AuthorizationFilter.matchEmpIdOrAdmin(
                currProject.getEmployee().getEmployeeID(), securityContext)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        String workPackageID = projectWorkPackageEstimate.
                getWorkPackage().getWorkpackageID();
        WorkPackage currWorkPackage = workPackageAccessor.
                find(workPackageID, projectID);
        if (currWorkPackage == null) {
            return Response.status(Response.Status.BAD_REQUEST).
                    entity("Workpackage does not exist").build();
        }

        projectWorkPackageEstimate.setWorkPackage(currWorkPackage);
        projectWorkPackageEstimateAccessor.persist(projectWorkPackageEstimate);
        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * Update project work package estimate response.
     *
     * @param projectWorkPackageEstimate the project work package estimate
     * @return the response
     */
    @PUT
    @Consumes("application/json")
    public Response updateProjectWorkPackageEstimate(
            ProjectWorkPackageEstimate projectWorkPackageEstimate) {
        ProjectWorkPackageEstimate currProjectWorkPackageEstimate
                = projectWorkPackageEstimateAccessor.
                findAllByWorkpackageId(
                        projectWorkPackageEstimate.getWorkPackage());
        if (currProjectWorkPackageEstimate == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Project currProject = projectAccessor.
                find(projectWorkPackageEstimate.getProject().getProjectID());
        if (currProject == null) {
            return Response.status(Response.Status.BAD_REQUEST).
                    entity("Project does not exist").build();
        }
        
        /*
         * If not admin or owner, return 403 - forbidden.
         */
        if (!AuthorizationFilter.matchEmpIdOrAdmin(
                currProject.getEmployee().getEmployeeID(), securityContext)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        
        WorkPackage workPackage = workPackageAccessor.
                find(projectWorkPackageEstimate.getWorkPackage().
                        getWorkpackageID(), currProject.getProjectID());
        if (workPackage == null) {
            return Response.status(Response.Status.NOT_FOUND).
                    entity("Work package not found").build();
        }

        projectWorkPackageEstimate.setWorkPackage(workPackage);

        projectWorkPackageEstimateAccessor.merge(projectWorkPackageEstimate);
        return Response.status(Response.Status.OK).build();
    }

    /**
     * Delete project work package estimate response.
     *
     * @param endDate       the end date
     * @param workpackageID the workpackage id
     * @param projectID     the project id
     * @return the response
     */
    @DELETE
    @Consumes("application/json")
    public Response deleteProjectWorkPackageEstimate(
            @QueryParam("workpackageID") final String workpackageID,
            @QueryParam("projectID") final String projectID) {
        if (workpackageID != null && projectID != null) {

            Project currProject = projectAccessor.find(projectID);
            if (currProject == null) {
                return Response.status(Response.Status.BAD_REQUEST).
                        entity("Project does not exist").build();
            }
            
            /*
             * If not admin or owner, return 403 - forbidden.
             */
            if (!AuthorizationFilter.matchEmpIdOrAdmin(
                    currProject.getEmployee().getEmployeeID(),
                    securityContext)) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
            
            WorkPackage workPackage = workPackageAccessor.
                    find(workpackageID, projectID);
            if (workPackage == null) {
                return Response.status(Response.Status.NOT_FOUND).
                        entity("Work package not found").build();
            }
            
            ProjectWorkPackageEstimate currentProjectWorkPackageEstimate =
                    projectWorkPackageEstimateAccessor.
                            findAllByWorkpackageId(workPackage);

            if (currentProjectWorkPackageEstimate == null) {
                return Response.status(Response.Status.NOT_FOUND).
                        entity("Project Work PackageEstimate not found").
                        build();
            }

            projectWorkPackageEstimateAccessor.
                    remove(currentProjectWorkPackageEstimate);
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).
                    entity("Missing params").build();
        }
    }
}
