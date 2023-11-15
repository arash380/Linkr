package com.linkr.services;


import com.linkr.access.ProjectAccessor;
import com.linkr.access.WorkPackageAccessor;
import com.linkr.access.WorkPackageEstimateCostsAccessor;
import com.linkr.models.Project;
import com.linkr.models.WorkPackage;
import com.linkr.models.WorkPackageEstimateCosts;
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
 * The type Work package estimate costs resource.
 * @author Team Linkr
 * @version 1.0
 */
@Path("/workpackage-estimate-costs")
@Stateless
@Authenticated
public class WorkPackageEstimateCostsResource {
    /**
     * Used to access security context and get request user based on provided JWT.
     */
    @Context
    private SecurityContext securityContext;

    /**
     * Used to access WPE information from database.
     */
    @Inject
    private WorkPackageEstimateCostsAccessor workPackageEstimateCostsAccessor;

    /**
     * Used to access WP information from database.
     */
    @Inject
    private WorkPackageAccessor workPackageAccessor;

    /**
     * Used to access project information from database.
     */
    @Inject
    private ProjectAccessor projectAccessor;

    /**
     * Gets work package estimate costs.
     *
     * @param workpackageID the workpackage id
     * @param projectID     the project id
     * @return the work package estimate costs
     */
    @GET
    @Produces("application/json")
    public Response getWorkPackageEstimateCosts(
            @QueryParam("workpackageID") final String workpackageID,
            @QueryParam("projectID") final String projectID) {
        
        WorkPackage workPackage = workPackageAccessor.
                find(workpackageID, projectID);
        if (workPackage == null) {
            return Response.status(Response.Status.NOT_FOUND).
                    entity("Work package not found").build();
        }
        
        if (workpackageID != null && projectID != null) {
            WorkPackageEstimateCosts workPackageEstimateCosts
                    = workPackageEstimateCostsAccessor.
                    findAllByWorkpackageId(workPackage);
            if (workPackageEstimateCosts == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return ResourceUtils.entityFoundResponseProvider(
                    workPackageEstimateCosts);
        } else {
            return Response.status(Response.Status.BAD_REQUEST).
                    entity("Missing search params").build();
        }
    }

    /**
     * Create work package estimate costs response.
     *
     * @param workPackageEstimateCosts the work package estimate costs
     * @return the response
     */
    @POST
    @Consumes("application/json")
    public Response createWorkPackageEstimateCosts(
            WorkPackageEstimateCosts workPackageEstimateCosts) {
        
        WorkPackageEstimateCosts currentWorkPackageEstimateCosts =
                workPackageEstimateCostsAccessor.
                        findAllByWorkpackageId(
                                workPackageEstimateCosts.getWorkPackage());

        if (currentWorkPackageEstimateCosts != null) {
            return Response.status(Response.Status.CONFLICT).build();
        }

        String projectID = workPackageEstimateCosts.
                getWorkPackage().getProject().getProjectID();
        Project currProject = projectAccessor.find(projectID);
        if (currProject == null) {
            return Response.status(Response.Status.BAD_REQUEST).
                    entity("Project does not exist").build();
        }

        String workPackageID = workPackageEstimateCosts.
                getWorkPackage().getWorkpackageID();
        WorkPackage currWorkPackage = workPackageAccessor.
                find(workPackageID, projectID);
        
        if (currWorkPackage == null) {
            return Response.status(Response.Status.BAD_REQUEST).
                    entity("Workpackage does not exist").build();
        }
        
        /*
         * If not admin or owner, return 403 - forbidden.
         */
        if (!AuthorizationFilter.matchEmpIdOrAdmin(
                currWorkPackage.getResponsibleEngineer().getEmployeeID(),
                securityContext)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        workPackageEstimateCosts.setWorkPackage(currWorkPackage);
        workPackageEstimateCostsAccessor.persist(workPackageEstimateCosts);
        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * Update work package estimate costs response.
     *
     * @param workPackageEstimateCosts the work package estimate costs
     * @return the response
     */
    @PUT
    @Consumes("application/json")
    public Response updateWorkPackageEstimateCosts(
            WorkPackageEstimateCosts workPackageEstimateCosts) {
        WorkPackageEstimateCosts currWorkPackageEstimateCosts
                = workPackageEstimateCostsAccessor.
                findAllByWorkpackageId(
                        workPackageEstimateCosts.getWorkPackage());
        if (currWorkPackageEstimateCosts == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Project currProject = projectAccessor.
                find(workPackageEstimateCosts.getProject().getProjectID());
        if (currProject == null) {
            return Response.status(Response.Status.BAD_REQUEST).
                    entity("Project does not exist").build();
        }
        
        WorkPackage workPackage = workPackageAccessor.
                find(workPackageEstimateCosts.getWorkPackage().
                        getWorkpackageID(), currProject.getProjectID());
        if (workPackage == null) {
            return Response.status(Response.Status.NOT_FOUND).
                    entity("Work package not found").build();
        }
        
        /*
         * If not admin or owner, return 403 - forbidden.
         */
        if (!AuthorizationFilter.matchEmpIdOrAdmin(
                workPackage.getResponsibleEngineer().getEmployeeID(),
                securityContext)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        workPackageEstimateCosts.setWorkPackage(workPackage);

        workPackageEstimateCostsAccessor.merge(workPackageEstimateCosts);
        return Response.status(Response.Status.OK).build();
    }

    /**
     * Delete work package estimate costs response.
     *
     * @param workpackageID the workpackage id
     * @param projectID     the project id
     * @return the response
     */
    @DELETE
    @Consumes("application/json")
    public Response deleteWorkPackageEstimateCosts(
            @QueryParam("workpackageID") final String workpackageID,
            @QueryParam("projectID") final String projectID) {
        if (workpackageID != null && projectID != null) {

            Project currProject = projectAccessor.find(projectID);
            if (currProject == null) {
                return Response.status(Response.Status.BAD_REQUEST).
                        entity("Project does not exist").build();
            }
            
            WorkPackage workPackage
                    = workPackageAccessor.find(workpackageID, projectID);
            if (workPackage == null) {
                return Response.status(Response.Status.NOT_FOUND).
                        entity("Work package not found").build();
            }
            
            /*
             * If not admin or owner, return 403 - forbidden.
             */
            if (!AuthorizationFilter.matchEmpIdOrAdmin(
                    workPackage.getResponsibleEngineer().getEmployeeID(),
                    securityContext)) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
            
            WorkPackageEstimateCosts currentworkPackageEstimateCosts =
                    workPackageEstimateCostsAccessor.
                            findAllByWorkpackageId(workPackage);

            if (currentworkPackageEstimateCosts == null) {
                return Response.status(Response.Status.NOT_FOUND).
                        entity("Project Work PackageEstimate not found").
                        build();
            }

            workPackageEstimateCostsAccessor.
                    remove(currentworkPackageEstimateCosts);
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).
                    entity("Missing params").build();
        }
    }
}
