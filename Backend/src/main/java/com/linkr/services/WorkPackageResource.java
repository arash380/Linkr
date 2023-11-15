package com.linkr.services;


import com.linkr.access.*;
import com.linkr.models.*;
import com.linkr.services.filters.Authenticated;
import com.linkr.services.utils.ResourceUtils;


import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

/**
 * The type Work package resource.
 * @author Team Linkr
 * @version 1.0
 */
@Path("/work-package")
@Stateless
@Authenticated
public class WorkPackageResource {

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
     * Used to access row information from database.
     */
    @Inject
    private TimesheetRowAccessor timesheetRowAccessor;

    /**
     * Used to access WPE information from database.
     */
    @Inject
    private WorkPackageEmployeeAccessor workPackageEmployeeAccessor;

    /**
     * Used to access PE information from database.
     */
    @Inject
    private ProjectWorkPackageEstimateAccessor
            projectWorkPackageEstimateAccessor;

    /**
     * Used to access WPC information from database.
     */
    @Inject
    private WorkPackageEstimateCostsAccessor
            workPackageEstimateCostsAccessor;

    /**
     * Used to access WPC information from database.
     */
    @Inject
    private WorkPackageEstimateToCompletionAccessor
            workPackageEstimateToCompletionAccessor;

    /**
     * Gets work package.
     *
     * @param wpId             the wp id
     * @param projId           the proj id
     * @param responsibleEngId the responsible eng id
     * @return the work package
     */
    @GET
    @Produces("application/json")
    public Response getWorkPackage(
            @QueryParam("wp-id") final String wpId,
            @QueryParam("proj-id") final String projId,
            @QueryParam("r-engineer-id") final String responsibleEngId) {
        if (wpId == null && projId == null && responsibleEngId == null) {
            List<WorkPackage> workPackages = workPackageAccessor.findAll();
            return ResourceUtils.entityFoundResponseProvider(workPackages);
        } else if (wpId == null && projId == null) {
            List<WorkPackage> workPackages = workPackageAccessor.
                    findByResponsibleEngineerId(responsibleEngId);
            return ResourceUtils.entitiesFoundResponseProvider(workPackages);
        } else if (wpId != null && projId != null) {
            WorkPackage workPackage = workPackageAccessor.find(wpId, projId);
            return ResourceUtils.entityFoundResponseProvider(workPackage);
        } else if (wpId == null) {
            List<WorkPackage> workPackages = workPackageAccessor.
                    findByProjectId(projId);
            return ResourceUtils.entitiesFoundResponseProvider(workPackages);
        } else {
            return Response.status(Response.Status.BAD_REQUEST).
                    entity("Accepted Query Parameter non-null "
                    + "combinations:" + "[wpId, projId], "
                    + "[r-engineer-id], " + "[]").build();
        }
    }

    /**
     * Gets children of work package.
     * @param wpId the id of the workpackage
     * @param projectID the project ID
     * @return the children
     */
    @GET
    @Path("{wpId}")
    @Produces("application/json")
    public Response getChildrenWorkPackage(
            @PathParam("wpId") String wpId,
            @QueryParam("projectId") String projectID
    ) {
        if (projectID == null) {
            return Response.status(Response.Status.BAD_REQUEST).
                    entity("Missing Project Id").build();
        }
        int indexOfZero = wpId.indexOf("0");
        if (indexOfZero == -1) {
            return Response.status(Response.Status.NO_CONTENT).
                    entity("This workpackage has no children").build();
        }
        List<WorkPackage> workPackages = workPackageAccessor.
                findChildrenWorkPackages(projectID, wpId);

        return ResourceUtils.entitiesFoundResponseProvider(workPackages);
    }

    /**
     * Create work package response.
     *
     * @param workPackage the work package
     * @return the response
     */
    @POST
    @Consumes("application/json")
    public Response createWorkPackage(WorkPackage workPackage) {
        WorkPackage wp = workPackageAccessor.find(
                workPackage.getWorkpackageID(),
                workPackage.getProject().getProjectID()
        );
        if (wp != null) {
            return Response.status(Response.Status.CONFLICT).build();
        }
        List<WorkPackage> parentWorkPackages =
                workPackageAccessor.findParentWorkPackages(
                        workPackage.getProject().getProjectID(),
                        workPackage.getWorkpackageID());
        if (parentWorkPackages.size() > 0) {
            for (WorkPackage parent: parentWorkPackages) {
                parent.setChargable(false);
            }
        }

        workPackage.setUnallocatedBudget(workPackage.getStartingBudget());

        Project project = projectAccessor.
                find(workPackage.getProject().getProjectID());

        int firstZeroIndex = workPackage.getWorkpackageID().indexOf("0");
        if (firstZeroIndex == -1) {
            firstZeroIndex = workPackage.getWorkpackageID().length();
        }
        if (firstZeroIndex == 1) {
            if (workPackage.getStartingBudget()
                    > project.getUnallocatedBudget()) {
                return Response.status(Response.Status.BAD_REQUEST).
                        entity("Work package budget is greater"
                                + " than remaining project budget").build();
            }
            project.setUnallocatedBudget(project.getUnallocatedBudget()
                    - workPackage.getStartingBudget());
            projectAccessor.merge(project);
        } else {
            String prefix = workPackage.getWorkpackageID().
                    substring(0, firstZeroIndex - 1);
            while (prefix.length() < workPackage.
                    getWorkpackageID().length()) {
                prefix += "0";
            }
            WorkPackage parent = workPackageAccessor.
                    find(prefix, workPackage.getProject().getProjectID());
            if (parent == null || parent.getUnallocatedBudget()
                    < workPackage.getStartingBudget()) {
                return Response.status(Response.Status.BAD_REQUEST).
                        entity("Work package budget is greater"
                                + " than remaining parent budget").build();
            }
            parent.setUnallocatedBudget(parent.getUnallocatedBudget()
                    - workPackage.getStartingBudget());
            workPackageAccessor.merge(parent);
        }

        workPackage.setProject(project);
        workPackage.setStartingBudget(workPackage.getUnallocatedBudget());
        workPackageAccessor.persist(workPackage);
        
        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * Update work package response.
     *
     * @param workPackage the work package
     * @return the response
     */
    @PUT
    @Consumes("application/json")
    public Response updateWorkPackage(WorkPackage workPackage) {
        WorkPackage wp = workPackageAccessor.find(
                workPackage.getWorkpackageID(),
                workPackage.getProject().getProjectID()
        );
        if (wp == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else if (wp.isCompleted()) {
            return Response.status(Response.Status.NOT_MODIFIED).build();
        }
        workPackage.setProject(projectAccessor.
                find(workPackage.getProject().getProjectID()));
        workPackageAccessor.merge(workPackage);
        
        return Response.status(Response.Status.OK).build();
    }

    /**
     * Delete work package response.
     *
     * @param wpID   the wp id
     * @param projID the proj id
     * @return the response
     */
    @DELETE
    @Consumes("application/json")
    public Response deleteWorkPackage(
            @QueryParam("wp-id") String wpID,
            @QueryParam("proj-id") String projID) {
        if (wpID == null || projID == null) {
            Response.status(Response.Status.BAD_REQUEST).
                    entity("Both wp-id and proj-id are required").build();
        }

        WorkPackage wp = workPackageAccessor.find(wpID, projID);

        if (wp == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        List<TimesheetRow> timesheetRows = timesheetRowAccessor.
                findByWorkPackageAndProject(wp);
        if (wp.isCompleted() || (timesheetRows != null
                && timesheetRows.size() >= 1)) {
            return Response.status(Response.Status.NOT_MODIFIED).build();
        }

        List<WorkPackageEmployee> workPackageEmployeeList =
                workPackageEmployeeAccessor.
                        findAllByWorkPackageAndProjectId(wp);
        if (workPackageEmployeeList != null
                && workPackageEmployeeList.size() >= 1) {
            return Response.status(Response.Status.NOT_MODIFIED).build();
        }

        List<TimesheetRow> childRows = timesheetRowAccessor.
                findWPChildrenTimesheetRows(wp);
        if (childRows.size() > 0) {
            return Response.status(Response.Status.CONFLICT).
                    entity("Work package has children packages with charges").
                    build();
        }

        ProjectWorkPackageEstimate projectWorkPackageEstimate =
                projectWorkPackageEstimateAccessor.findAllByWorkpackageId(wp);

        if (projectWorkPackageEstimate != null) {
            projectWorkPackageEstimateAccessor.remove(projectWorkPackageEstimate);
        }

        WorkPackageEstimateCosts workPackageEstimateCosts =
                workPackageEstimateCostsAccessor.findAllByWorkpackageId(wp);

        if (workPackageEstimateCosts != null) {
            workPackageEstimateCostsAccessor.remove(workPackageEstimateCosts);
        }

        WorkPackageEstimateToCompletion
                workPackageEstimateToCompletion =
                workPackageEstimateToCompletionAccessor.
                findAllByWorkpackageId(wp);

        if (workPackageEstimateToCompletion != null) {
            workPackageEstimateToCompletionAccessor.
                    remove(workPackageEstimateToCompletion);
        }

        List<WorkPackage> childWorkPackages = workPackageAccessor.
                findChildrenWorkPackages(projID, wpID);
        for (WorkPackage child : childWorkPackages) {
            workPackageAccessor.remove(child);
        }
        
        wp.setProject(projectAccessor.
                find(wp.getProject().getProjectID()));
        workPackageAccessor.remove(wp);
        return Response.status(Response.Status.OK).build();
    }
}
