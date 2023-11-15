package com.linkr.services;

import com.linkr.access.*;
import com.linkr.models.*;
import com.linkr.services.filters.Authenticated;
import com.linkr.services.utils.ResourceUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * The type Project resource.
 * @author Team 4911
 * @version 1.0
 */
@Path("/project")
@Stateless
@Authenticated
public class ProjectResource {

    /**
     * Number of hours in a day.
     */
    private static final int HOURS_IN_DAY = 24;

    /**
     * To convert a number to a percentage.
     */
    private static final int CONVERT_TO_PERCENTAGE = 100;

    /**
     * An injected project accessor to access project table.
     */
    @Inject
    private ProjectAccessor projectAccessor;

    /**
     * An injected employee accessor to access employee table.
     */
    @Inject
    private EmployeeAccessor employeeAccessor;

    /**
     * An injected wp accessor to access wp table.
     */
    @Inject
    private WorkPackageAccessor workPackageAccessor;

    /**
     * An injected timesheet row accessor to access tr table.
     */
    @Inject
    private TimesheetRowAccessor timesheetRowAccessor;

    /**
     * An injected timesheet accessor to access timesheet table.
     */
    @Inject
    private TimesheetAccessor timesheetAccessor;

    /**
     * An injected project estimate accessor to access PE table.
     */
    @Inject
    private ProjectWorkPackageEstimateAccessor
            projectWorkPackageEstimateAccessor;

    /**
     * An injected EC accessor to access EC table.
     */
    @Inject
    private WorkPackageEstimateToCompletionAccessor
            workPackageEstimateToCompletionAccessor;

    /**
     * An injected WPE accessor to access WPE table.
     */
    @Inject
    private WorkPackageEstimateCostsAccessor
            workPackageEstimateCostsAccessor;

    /**
     * An injected rate sheet accessor to access RS table.
     */
    @Inject
    private RateSheetAccessor rateSheetAccessor;

    /**
     * Gets project.
     *
     * @param id the id
     * @return the project
     */
    @GET
    @Path("{proj-id}")
    @Produces("application/json")
    public Response getProject(@PathParam("proj-id") String id) {
        Project project = projectAccessor.find(id);

        return ResourceUtils.entityFoundResponseProvider(project);
    }

    /**
     * Gets all projects.
     *
     * @param empID the emp id
     * @return the all projects
     */
    @GET
    @Produces("application/json")
    public Response getAllProjects(@QueryParam("emp-id") Integer empID) {

        List<Project> projects;

        if (empID != null) {
            projects = projectAccessor.findByEmpID(empID);
        } else {
            projects = projectAccessor.findAll();
        }

        return ResourceUtils.entitiesFoundResponseProvider(projects);
    }

    /**
     * Gets project report.
     *
     * @param id the id
     * @return the project report
     */
    @GET
    @Path("/report/{proj-id}")
    @Produces("application/json")
    public Response getProjectReport(final @PathParam("proj-id") String id) {
        Project project = projectAccessor.find(id);
        if (project == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<WorkPackage> workPackages = workPackageAccessor.
                findByProjectId(id);

        List<ProjectWorkPackageReport> projectWorkPackageReports =
                new ArrayList<>();

        for (WorkPackage wp : workPackages) {
            ProjectWorkPackageReport projectWorkPackageReport
                    = new ProjectWorkPackageReport();
            WorkPackageReport parentWP = new WorkPackageReport();
            getListsFromWorkPackage(wp, parentWP);

            List<WorkPackage> allChildPackages =
                    workPackageAccessor.findAllChildrenWorkPackages(
                            project.getProjectID(),
                            parentWP.getWorkPackageId());

            List<WorkPackage> childPackages =
                    workPackageAccessor.findChildrenWorkPackages(
                            project.getProjectID(),
                            parentWP.getWorkPackageId());

            if (childPackages.size() > 0) {
                getListsFromChildWorkPackages(allChildPackages, wp, parentWP);
                List<WorkPackageReport> childWorkPackageReports
                        = new ArrayList<>();
                for (WorkPackage child : childPackages) {
                    WorkPackageReport childWp = new WorkPackageReport();
                    List<WorkPackage> grandChildWorkPackages =
                            workPackageAccessor.findChildrenWorkPackages(
                                    project.getProjectID(),
                                    child.getWorkpackageID());
                    if (grandChildWorkPackages.size() > 0) {
                        childWp.setWorkPackageName(
                                child.getWorkPackageTitle());
                        childWp.setWorkPackageId(
                                child.getWorkpackageID());
                        getListsFromChildWorkPackages(grandChildWorkPackages,
                                child, childWp);
                    } else {
                        getListsFromWorkPackage(child, childWp);
                    }
                    childWorkPackageReports.add(childWp);
                }
                projectWorkPackageReport.
                        setChildrenWorkPackages(childWorkPackageReports);
            } else {
                getListsFromWorkPackage(wp, parentWP);
            }
            projectWorkPackageReport.setParentWorkPackage(parentWP);
            projectWorkPackageReports.add(projectWorkPackageReport);
        }
        ProjectReport projectReport = new ProjectReport(
                projectWorkPackageReports, project.getProjectName());
        return ResourceUtils.entityFoundResponseProvider(projectReport);
    }

    private void getListsFromWorkPackage(WorkPackage workPackage,
                                         WorkPackageReport report) {
        report.setWorkPackageName(workPackage.getWorkPackageTitle());
        report.setWorkPackageId(workPackage.getWorkpackageID());
        List<TimesheetRow> timesheetRows =
                timesheetRowAccessor.
                        findWPChildrenTimesheetRows(workPackage);
        calculateTimsheetRowCosts(report, timesheetRows);

        WorkPackageEstimateToCompletion
                workPackageEstimateToCompletion
                = workPackageEstimateToCompletionAccessor.
                findAllByWorkpackageId(workPackage);

        if (workPackageEstimateToCompletion != null) {
            workPackageEstimateToCompletionCalculator(report,
                    workPackageEstimateToCompletion,
                    workPackage.getWorkpackageStartDate().getYear());
        }


        WorkPackageEstimateCosts
                workPackageEstimateCosts
                = workPackageEstimateCostsAccessor.
                findAllByWorkpackageId(workPackage);

        if (workPackageEstimateCosts != null) {
            workPackageEstimateCalculator(report,
                    workPackageEstimateCosts,
                    workPackage.getWorkpackageStartDate().getYear());
        }


        ProjectWorkPackageEstimate
                projectWorkPackageEstimate
                = projectWorkPackageEstimateAccessor.
                findAllByWorkpackageId(workPackage);

        if (projectWorkPackageEstimate != null) {
            projectWorkPackageEstimateCalculator(report,
                    projectWorkPackageEstimate,
                    workPackage.getWorkpackageStartDate().getYear());
        }

        if (report.getProjectBudgetDays() == 0) {
            report.setVarianceDays(0);
        } else {
            float varianceDays = (report.getDaysEstimatedAtCompletion()
                    - report.getProjectBudgetDays())
                    / report.getProjectBudgetDays()
                    * CONVERT_TO_PERCENTAGE;
            report.setVarianceDays((int) varianceDays);
        }

        if (report.getProjectBudgetLabourCost() == 0) {
            report.setVarianceCost(0);
        } else {
            float varianceCost = (report.getCostEstimatedAtCompletion()
                    - report.getProjectBudgetLabourCost())
                    / report.getProjectBudgetLabourCost()
                    * CONVERT_TO_PERCENTAGE;
            report.setVarianceCost((int) varianceCost);
        }

        if (workPackage.isCompleted()) {
            report.setPercentageComplete(CONVERT_TO_PERCENTAGE);
        }
    }

    private void getListsFromChildWorkPackages(List<WorkPackage> workPackages,
                                         WorkPackage parentWorkPackage,
                                         WorkPackageReport report) {
        List<TimesheetRow> parentTimesheetRows =
                timesheetRowAccessor.
                        findWPChildrenTimesheetRows(parentWorkPackage);
        calculateTimsheetRowCosts(report, parentTimesheetRows);
        for (WorkPackage workPackage : workPackages) {
            List<TimesheetRow> timesheetRows =
                    timesheetRowAccessor.
                            findWPChildrenTimesheetRows(workPackage);
            calculateTimsheetRowCosts(report, timesheetRows);

            WorkPackageEstimateToCompletion
                    workPackageEstimateToCompletion
                    = workPackageEstimateToCompletionAccessor.
                    findAllByWorkpackageId(workPackage);

            if (workPackageEstimateToCompletion != null) {
                workPackageEstimateToCompletionCalculator(report,
                        workPackageEstimateToCompletion,
                        workPackage.getWorkpackageStartDate().getYear());
            }


            WorkPackageEstimateCosts
                    workPackageEstimateCosts
                    = workPackageEstimateCostsAccessor.
                    findAllByWorkpackageId(workPackage);

            if (workPackageEstimateCosts != null) {
                workPackageEstimateCalculator(report,
                        workPackageEstimateCosts,
                        workPackage.getWorkpackageStartDate().getYear());
            }


            ProjectWorkPackageEstimate
                    projectWorkPackageEstimate
                    = projectWorkPackageEstimateAccessor.
                    findAllByWorkpackageId(workPackage);

            if (projectWorkPackageEstimate != null) {
                projectWorkPackageEstimateCalculator(report,
                        projectWorkPackageEstimate,
                        workPackage.getWorkpackageStartDate().getYear());
            }

            if (workPackage.isCompleted()) {
                report.setPercentageComplete(CONVERT_TO_PERCENTAGE);
            }
        }

        report.setPercentageComplete(report.
                getPercentageComplete() / workPackages.size());

        float varianceDays = (report.getDaysEstimatedAtCompletion()
                - report.getProjectBudgetDays())
                / report.getProjectBudgetDays()
                * CONVERT_TO_PERCENTAGE;
        report.setVarianceDays((int) varianceDays);

        float varianceCost = (report.getCostEstimatedAtCompletion()
                - report.getProjectBudgetLabourCost())
                / report.getProjectBudgetLabourCost()
                * CONVERT_TO_PERCENTAGE;
        report.setVarianceCost((int) varianceCost);
    }

    private void calculateTimsheetRowCosts(
            WorkPackageReport report,
            List<TimesheetRow> rows) {
        for (TimesheetRow row : rows) {
            Timesheet timesheet = timesheetAccessor.
                    find(row.getTimesheet().getTimesheetID());
            if (timesheet.isManagerApproval()) {
                float[] hours = row.getHoursInDays();
                float totalHours = 0;
                for (Float hour : hours) {
                    totalHours += hour;
                }
                float days = totalHours / HOURS_IN_DAY;
                Employee employee = employeeAccessor.
                        find(row.getTimesheet().getEmployee().getEmployeeID());
                float rate = rateSheetAccessor.
                        find(employee.getPayRate().
                                toUpperCase(Locale.ROOT),
                                row.getTimesheet().getEndDate().getYear());
                float cost = totalHours * rate;
                report.setActualToDateDays(days);
                report.setActualToDateCost(cost);
            }
        }
    }

    private void workPackageEstimateToCompletionCalculator(
            WorkPackageReport report,
            WorkPackageEstimateToCompletion
                    workPackageEstimateToCompletion,
            int year) {
        float days;
        float cost;
        float p1 = workPackageEstimateToCompletion.getP1PlannedDays();
        float p2 = workPackageEstimateToCompletion.getP2PlannedDays();
        float p3 = workPackageEstimateToCompletion.getP3PlannedDays();
        float p4 = workPackageEstimateToCompletion.getP4PlannedDays();
        float p5 = workPackageEstimateToCompletion.getP5PlannedDays();
        float js = workPackageEstimateToCompletion.getJsPlannedDays();
        float ss = workPackageEstimateToCompletion.getSsPlannedDays();
        float ds = workPackageEstimateToCompletion.getDsPlannedDays();
        days = p1 + p2 + p3 + p4 + p5 + js + ss + ds;
        float p1Cost = p1 * rateSheetAccessor.
                find("P1", year);
        float p2Cost = p2 * rateSheetAccessor.
                find("P2", year);
        float p3Cost = p3 * rateSheetAccessor.
                find("P3", year);
        float p4Cost = p4 * rateSheetAccessor.
                find("P4", year);
        float p5Cost = p5 * rateSheetAccessor.
                find("P5", year);
        float jsCost = js * rateSheetAccessor.
                find("JS", year);
        float ssCost = ss * rateSheetAccessor.
                find("SS", year);
        float dsCost = ds * rateSheetAccessor.
                find("DS", year);
        cost = p1Cost + p2Cost + p3Cost + p4Cost
                + p5Cost + jsCost + ssCost + dsCost;

        report.setDaysEstimatedAtCompletion(report.
                getDaysEstimatedAtCompletion() + days
                + report.getActualToDateDays());
        report.setCostEstimatedAtCompletion(report.
                getCostEstimatedAtCompletion() + cost
                + report.getActualToDateCost());

        report.setPercentageComplete(report.
                getPercentageComplete() + workPackageEstimateToCompletion.
                getPercentageComplete());
    }

    private void workPackageEstimateCalculator(
            WorkPackageReport report,
            WorkPackageEstimateCosts
                    workPackageEstimateCosts,
            int year) {
        float days;
        float cost;
        float p1 = workPackageEstimateCosts.getP1PlannedDays();
        float p2 = workPackageEstimateCosts.getP2PlannedDays();
        float p3 = workPackageEstimateCosts.getP3PlannedDays();
        float p4 = workPackageEstimateCosts.getP4PlannedDays();
        float p5 = workPackageEstimateCosts.getP5PlannedDays();
        float js = workPackageEstimateCosts.getJsPlannedDays();
        float ss = workPackageEstimateCosts.getSsPlannedDays();
        float ds = workPackageEstimateCosts.getDsPlannedDays();
        days = p1 + p2 + p3 + p4 + p5 + js + ss + ds;
        float p1Cost = p1 * rateSheetAccessor.
                find("P1", year);
        float p2Cost = p2 * rateSheetAccessor.
                find("P2", year);
        float p3Cost = p3 * rateSheetAccessor.
                find("P3", year);
        float p4Cost = p4 * rateSheetAccessor.
                find("P4", year);
        float p5Cost = p5 * rateSheetAccessor.
                find("P5", year);
        float jsCost = js * rateSheetAccessor.
                find("JS", year);
        float ssCost = ss * rateSheetAccessor.
                find("SS", year);
        float dsCost = ds * rateSheetAccessor.
                find("DS", year);
        cost = p1Cost + p2Cost + p3Cost + p4Cost
                + p5Cost + jsCost + ssCost + dsCost;

        report.setEngineersEstimatedDays(days);
        report.setEngineersEstimatedCost(cost);
    }

    private void projectWorkPackageEstimateCalculator(
            WorkPackageReport report,
            ProjectWorkPackageEstimate projectWorkPackageEstimateList,
            int year) {
        float days;
        float cost;
        float p1 = projectWorkPackageEstimateList.getP1PlannedDays();
        float p2 = projectWorkPackageEstimateList.getP2PlannedDays();
        float p3 = projectWorkPackageEstimateList.getP3PlannedDays();
        float p4 = projectWorkPackageEstimateList.getP4PlannedDays();
        float p5 = projectWorkPackageEstimateList.getP5PlannedDays();
        float js = projectWorkPackageEstimateList.getJsPlannedDays();
        float ss = projectWorkPackageEstimateList.getSsPlannedDays();
        float ds = projectWorkPackageEstimateList.getDsPlannedDays();
        days = p1 + p2 + p3 + p4 + p5 + js + ss + ds;
        float p1Cost = p1 * rateSheetAccessor.
                find("P1", year);
        float p2Cost = p2 * rateSheetAccessor.
                find("P2", year);
        float p3Cost = p3 * rateSheetAccessor.
                find("P3", year);
        float p4Cost = p4 * rateSheetAccessor.
                find("P4", year);
        float p5Cost = p5 * rateSheetAccessor.
                find("P5", year);
        float jsCost = js * rateSheetAccessor.
                find("JS", year);
        float ssCost = ss * rateSheetAccessor.
                find("SS", year);
        float dsCost = ds * rateSheetAccessor.
                find("DS", year);
        cost = p1Cost + p2Cost + p3Cost + p4Cost
                + p5Cost + jsCost + ssCost + dsCost;

        report.setProjectBudgetDays(days);
        report.setProjectBudgetLabourCost(cost);
    }

    /**
     * Create project response.
     *
     * @param project the project
     * @return the response
     */
    @POST
    @Consumes("application/json")
    public Response createProject(Project project) {

        Project proj = projectAccessor.find(project.getProjectID());
        if (proj != null) {
            return Response.status(Response.Status.CONFLICT).build();
        }

        if (project.getEmployee() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        project.setEmployee(employeeAccessor.
                find(project.getEmployee().getEmployeeID()));

        if (project.getProjectManagerAssistant() != null) {
            project.setProjectManagerAssistant(employeeAccessor.
                    find(project.getProjectManagerAssistant().getEmployeeID()));
        }



        projectAccessor.persist(project);
        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * Update project response.
     *
     * @param project the project
     * @return the response
     */
    @PUT
    @Consumes("application/json")
    public Response updateProject(Project project) {
        Project proj = projectAccessor.find(project.getProjectID());
        if (proj == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (project.getEmployee() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        project.setEmployee(employeeAccessor.
                find(project.getEmployee().getEmployeeID()));

        if (project.getProjectManagerAssistant() != null) {
            project.setProjectManagerAssistant(employeeAccessor.
                    find(project.getProjectManagerAssistant().getEmployeeID()));
        }

        if (!project.isActiveProject()) {
            List<WorkPackage> packages = workPackageAccessor.
                    findByProjectId(project.getProjectID());
            for (WorkPackage p : packages) {
                p.setCompleted(true);
                workPackageAccessor.merge(p);
            }
        }

        projectAccessor.merge(project);
        return Response.status(Response.Status.OK).build();
    }

    /**
     * Delete project response.
     *
     * @param projectID the project id
     * @return the response
     */
    @DELETE
    @Path("{proj-id}")
    @Consumes("application/json")
    public Response deleteProject(@PathParam("proj-id") String projectID) {
        Project project = projectAccessor.find(projectID);
        if (project == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if (project.getEmployee() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        project.setEmployee(employeeAccessor.
                find(project.getEmployee().getEmployeeID()));

        if (project.getProjectManagerAssistant() != null) {
            project.setProjectManagerAssistant(employeeAccessor.
                    find(project.getProjectManagerAssistant().getEmployeeID()));
        }

        projectAccessor.remove(project);
        return Response.status(Response.Status.OK).build();
    }
}
