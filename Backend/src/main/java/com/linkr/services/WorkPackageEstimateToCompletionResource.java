package com.linkr.services;

import com.linkr.access.ProjectAccessor;
import com.linkr.access.WorkPackageAccessor;
import com.linkr.access.WorkPackageEstimateToCompletionAccessor;
import com.linkr.models.Project;
import com.linkr.models.WorkPackage;
import com.linkr.models.WorkPackageEstimateToCompletion;
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
 * The type Work package estimate to completion resource.
 * @author Team Linkr
 * @version 1.0
 */
@Path("/workpackage-estimate-to-completion")
@Stateless
@Authenticated
public class WorkPackageEstimateToCompletionResource {

    /**
     * Used to access security context and get request
     * user based on provided JWT.
     */
    @Context
    private SecurityContext securityContext;

    /**
     * Used to access EC information from database.
     */
    @Inject
    private WorkPackageEstimateToCompletionAccessor
            workPackageEstimateToCompletionAccessor;

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
     * Gets work package estimate to completion.
     *
     * @param workpackageID the workpackage id
     * @param projectID     the project id
     * @return the work package estimate to completion
     */
    @GET
    @Produces("application/json")
    public Response getWorkPackageEstimateToCompletion(
            @QueryParam("workpackageID") final String workpackageID,
            @QueryParam("projectID") final String projectID) {

        WorkPackage workPackage = workPackageAccessor.
                find(workpackageID, projectID);
        if (workPackage == null) {
            return Response.status(Response.Status.NOT_FOUND).
                    entity("Work package not found").build();
        }

        if (workpackageID != null && projectID != null) {
            WorkPackageEstimateToCompletion workPackageEstimateToCompletion
                    = workPackageEstimateToCompletionAccessor.
                    findAllByWorkpackageId(workPackage);
            if (workPackageEstimateToCompletion == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return ResourceUtils.
                    entityFoundResponseProvider(
                            workPackageEstimateToCompletion);
        } else {
            return Response.status(Response.Status.BAD_REQUEST).
                    entity("Missing search params").build();
        }
    }

    /**
     * Create work package estimate to completion response.
     *
     * @param workPackageEstimateToCompletion the
     *                                       work package estimate to completion
     * @return the response
     */
    @POST
    @Consumes("application/json")
    public Response createWorkPackageEstimateToCompletion(
            final WorkPackageEstimateToCompletion
                    workPackageEstimateToCompletion) {

        WorkPackageEstimateToCompletion currentWPEstimateToCompletion =
                workPackageEstimateToCompletionAccessor.
                        findAllByWorkpackageId(
                                workPackageEstimateToCompletion.
                                        getWorkPackage());

        if (currentWPEstimateToCompletion != null) {
            return Response.status(Response.Status.CONFLICT).build();
        }

        String projectID = workPackageEstimateToCompletion.
                getWorkPackage().getProject().getProjectID();
        Project currProject = projectAccessor.find(projectID);
        if (currProject == null) {
            return Response.status(Response.Status.BAD_REQUEST).
                    entity("Project does not exist").build();
        }

        String workPackageID = workPackageEstimateToCompletion.
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
                currWorkPackage.getResponsibleEngineer().
                        getEmployeeID(), securityContext)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        workPackageEstimateToCompletion.setWorkPackage(currWorkPackage);
        workPackageEstimateToCompletionAccessor.
                persist(workPackageEstimateToCompletion);
        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * Update work package estimate to completion response.
     *
     * @param workPackageEstimateToCompletion the
     *                                       work package estimate to completion
     * @return the response
     */
    @PUT
    @Consumes("application/json")
    public Response updateWorkPackageEstimateToCompletion(
            final WorkPackageEstimateToCompletion
                     workPackageEstimateToCompletion) {
        WorkPackageEstimateToCompletion currWPETC
                = workPackageEstimateToCompletionAccessor.
                findAllByWorkpackageId(
                        workPackageEstimateToCompletion.getWorkPackage());
        if (currWPETC == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Project currProject = projectAccessor.
                find(workPackageEstimateToCompletion.
                        getProject().getProjectID());
        if (currProject == null) {
            return Response.status(Response.Status.BAD_REQUEST).
                    entity("Project does not exist").build();
        }

        WorkPackage workPackage = workPackageAccessor.
                find(workPackageEstimateToCompletion.getWorkPackage().
                        getWorkpackageID(), currProject.getProjectID());
        if (workPackage == null) {
            return Response.status(Response.Status.NOT_FOUND).
                    entity("Work package not found").build();
        }

        /*
         * If not admin or owner, return 403 - forbidden.
         */
        if (!AuthorizationFilter.matchEmpIdOrAdmin(
                workPackage.getResponsibleEngineer().
                        getEmployeeID(), securityContext)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        workPackageEstimateToCompletion.setWorkPackage(workPackage);

        workPackageEstimateToCompletionAccessor.
                merge(workPackageEstimateToCompletion);
        return Response.status(Response.Status.OK).build();
    }

    /**
     * Delete work package estimate to completion response.
     *
     * @param workpackageID the workpackage id
     * @param projectID     the project id
     * @return the response
     */
    @DELETE
    @Consumes("application/json")
    public Response deleteWorkPackageEstimateToCompletion(
            @QueryParam("workpackageID") final String workpackageID,
            @QueryParam("projectID") final String projectID) {
        if (workpackageID != null && projectID != null) {

            Project currProject = projectAccessor.find(projectID);
            if (currProject == null) {
                return Response.status(Response.Status.BAD_REQUEST).
                        entity("Project does not exist").build();
            }

            WorkPackage workPackage = workPackageAccessor.
                    find(workpackageID, projectID);
            if (workPackage == null) {
                return Response.status(Response.Status.NOT_FOUND).
                        entity("Work package not found").build();
            }

            /*
             * If not admin or owner, return 403 - forbidden.
             */
            if (!AuthorizationFilter.matchEmpIdOrAdmin(
                    workPackage.getResponsibleEngineer().
                            getEmployeeID(), securityContext)) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }

            WorkPackageEstimateToCompletion currWPETC =
                    workPackageEstimateToCompletionAccessor.
                            findAllByWorkpackageId(workPackage);

            if (currWPETC == null) {
                return Response.status(Response.Status.NOT_FOUND).
                        entity("WorkPackage Estimate To Completion not found").
                        build();
            }

            workPackageEstimateToCompletionAccessor.remove(currWPETC);
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).
                    entity("Missing params").build();
        }
    }

}
