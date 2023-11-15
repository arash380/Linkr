package com.linkr.models;

import com.fasterxml.jackson.annotation.*;

import java.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * ProjectWorkPackageEstimate entity.
 * @author Team Linkr.
 * @version 1.0
 */
@Entity
@IdClass(ProjectWorkPackageEstimateId.class)
@Table(name = "project_work_package_estimate")
public class ProjectWorkPackageEstimate {

    /**
     * project.
     */
    @Transient
    @JsonProperty("projectID")
    @JsonIdentityInfo(generator = ObjectIdGenerators.
            PropertyGenerator.class, property = "projectID")
    @JsonIdentityReference(alwaysAsId = true)
    private Project project;

    /**
     * wp.
     */
    @Id
    @NotNull
    @ManyToOne
    @JsonProperty("workPackageID")
    @JsonIdentityInfo(generator = ObjectIdGenerators.
            PropertyGenerator.class, property = "workpackageID")
    @JsonIdentityReference(alwaysAsId = true)
    private WorkPackage workPackage;

    /**
     * planned days.
     */
    private float p1PlannedDays;

    /**
     * planned days.
     */
    private float p2PlannedDays;

    /**
     * planned days.
     */
    private float p3PlannedDays;

    /**
     * planned days.
     */
    private float p4PlannedDays;

    /**
     * planned days.
     */
    private float p5PlannedDays;

    /**
     * planned days.
     */
    private float jsPlannedDays;

    /**
     * planned days.
     */
    private float ssPlannedDays;

    /**
     * planned days.
     */
    private float dsPlannedDays;

    /**
     * Gets project.
     *
     * @return the projectID
     */
    public Project getProject() {
        if (project == null && workPackage != null) {
            if (workPackage.getProject() != null) {
                setProject(workPackage.getProject().getProjectID());
            }
        }
        return project;
    }

    /**
     * Sets project.
     *
     * @param project the project ID to set
     */
    public void setProject(Project project) {
        this.project = new Project();
        this.project.setProjectID(project.getProjectID());
        if (this.getWorkPackage() != null
                && this.workPackage.getProject() == null) {
            this.workPackage.setProject(this.project);
        }
    }

    /**
     * Sets project.
     *
     * @param projectID the project ID to set
     */
    @JsonProperty("projectID")
    public void setProject(String projectID) {
        this.project = new Project();
        this.project.setProjectID(projectID);
        if (this.getWorkPackage() != null
                && this.workPackage.getProject() == null) {
            this.workPackage.setProject(this.project);
        }

        if (this.workPackage != null) {
            this.workPackage.setProject(this.project);
        }
    }

    /**
     * Gets work package.
     *
     * @return the workPackage
     */
    public WorkPackage getWorkPackage() {
        return workPackage;
    }

    /**
     * Sets work package.
     *
     * @param workPackage the workPackage to set
     */
    public void setWorkPackage(WorkPackage workPackage) {
        this.workPackage = workPackage;
    }

    /**
     * Sets work package.
     *
     * @param workPackageID the workPackage ID to set
     */
    @JsonProperty("workPackageID")
    public void setWorkPackage(String workPackageID) {
        this.workPackage = new WorkPackage();
        this.workPackage.setWorkpackageID(workPackageID);

        if (this.project != null) {
            this.workPackage.setProject(this.project);
        }
    }

    /**
     * Gets p 1 planned days.
     *
     * @return the p1PlannedDays
     */
    public float getP1PlannedDays() {
        return p1PlannedDays;
    }

    /**
     * Sets p 1 planned days.
     *
     * @param p1PlannedDays the p1PlannedDays to set
     */
    public void setP1PlannedDays(float p1PlannedDays) {
        this.p1PlannedDays = p1PlannedDays;
    }

    /**
     * Gets p 2 planned days.
     *
     * @return the p2PlannedDays
     */
    public float getP2PlannedDays() {
        return p2PlannedDays;
    }

    /**
     * Sets p 2 planned days.
     *
     * @param p2PlannedDays the p2PlannedDays to set
     */
    public void setP2PlannedDays(float p2PlannedDays) {
        this.p2PlannedDays = p2PlannedDays;
    }

    /**
     * Gets p 3 planned days.
     *
     * @return the p3PlannedDays
     */
    public float getP3PlannedDays() {
        return p3PlannedDays;
    }

    /**
     * Sets p 3 planned days.
     *
     * @param p3PlannedDays the p3PlannedDays to set
     */
    public void setP3PlannedDays(float p3PlannedDays) {
        this.p3PlannedDays = p3PlannedDays;
    }

    /**
     * Gets p 4 planned days.
     *
     * @return the p4PlannedDays
     */
    public float getP4PlannedDays() {
        return p4PlannedDays;
    }

    /**
     * Sets p 4 planned days.
     *
     * @param p4PlannedDays the p4PlannedDays to set
     */
    public void setP4PlannedDays(float p4PlannedDays) {
        this.p4PlannedDays = p4PlannedDays;
    }

    /**
     * Gets p 5 planned days.
     *
     * @return the p5PlannedDays
     */
    public float getP5PlannedDays() {
        return p5PlannedDays;
    }

    /**
     * Sets p 5 planned days.
     *
     * @param p5PlannedDays the p5PlannedDays to set
     */
    public void setP5PlannedDays(float p5PlannedDays) {
        this.p5PlannedDays = p5PlannedDays;
    }

    /**
     * Gets js planned days.
     *
     * @return the jsPlannedDays
     */
    public float getJsPlannedDays() {
        return jsPlannedDays;
    }

    /**
     * Sets js planned days.
     *
     * @param jsPlannedDays the jsPlannedDays to set
     */
    public void setJsPlannedDays(float jsPlannedDays) {
        this.jsPlannedDays = jsPlannedDays;
    }

    /**
     * Gets ss planned days.
     *
     * @return the ssPlannedDays
     */
    public float getSsPlannedDays() {
        return ssPlannedDays;
    }

    /**
     * Sets ss planned days.
     *
     * @param ssPlannedDays the ssPlannedDays to set
     */
    public void setSsPlannedDays(float ssPlannedDays) {
        this.ssPlannedDays = ssPlannedDays;
    }

    /**
     * Gets ds planned days.
     *
     * @return the dsPlannedDays
     */
    public float getDsPlannedDays() {
        return dsPlannedDays;
    }

    /**
     * Sets ds planned days.
     *
     * @param dsPlannedDays the dsPlannedDays to set
     */
    public void setDsPlannedDays(float dsPlannedDays) {
        this.dsPlannedDays = dsPlannedDays;
    }
}
