package com.linkr.models;

import com.fasterxml.jackson.annotation.*;

import java.time.LocalDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * WorkPackage entity.
 *
 * @author Team Linkr
 * @version 1.0
 */
@Entity
@IdClass(WorkPackageId.class)
@Table(name = "work_package")
public class WorkPackage implements Comparable<WorkPackage> {

    /**
     * work package.
     */
    @Id
    @NotNull
    @Column(name = "ID")
    private String workpackageID;

    /**
     * project.
     */
    @Id
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonProperty("projectID")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "projectID")
    @JsonIdentityReference(alwaysAsId = true)
    private Project project;

    /**
     * employee.
     */
    @NotNull
    @ManyToOne
    @JsonProperty("responsibleEngineerID")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "employeeID")
    @JsonIdentityReference(alwaysAsId = true)
    private Employee responsibleEngineer;

    /**
     * title.
     */
    private String workPackageTitle;

    /**
     * budget.
     */
    private float unallocatedBudget;

    /**
     * budget.
     */
    private float startingBudget;

    /**
     * chargeable.
     */
    private boolean chargable;

    /**
     * start date.
     */
    private LocalDate workpackageStartDate;

    /**
     * end date.
     */
    private LocalDate workpackageEndDate;

    /**
     * completed.
     */
    private boolean completed;


    /**
     * Gets workpackage id.
     *
     * @return the workPackageID
     */
    public String getWorkpackageID() {
        return workpackageID;
    }

    /**
     * Sets workpackage id.
     *
     * @param workpackageID the workPackageID to set
     */
    public void setWorkpackageID(String workpackageID) {
        this.workpackageID = workpackageID;
    }

    /**
     * Gets project.
     *
     * @return the projectID
     */
    public Project getProject() {
        return project;
    }

    /**
     * Sets project.
     *
     * @param project the project to set
     */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * Sets project.
     *
     * @param projectID the project ID to set
     */
    public void setProject(String projectID) {
        this.project = new Project();
        this.project.setProjectID(projectID);
    }

    /**
     * Gets responsible engineer.
     *
     * @return the employeeID
     */
    public Employee getResponsibleEngineer() {
        return responsibleEngineer;
    }

    /**
     * Sets responsible engineer.
     *
     * @param responsibleEngineer the employee to set
     */
    public void setResponsibleEngineer(Employee responsibleEngineer) {
        this.responsibleEngineer = responsibleEngineer;
    }

    /**
     * Sets responsible engineer.
     *
     * @param employeeID the employee ID to set
     */
    public void setResponsibleEngineer(int employeeID) {
        this.responsibleEngineer = new Employee();
        this.responsibleEngineer.setEmployeeID(employeeID);
    }

    /**
     * Gets work package title.
     *
     * @return the workPackageTitle
     */
    public String getWorkPackageTitle() {
        return workPackageTitle;
    }

    /**
     * Sets work package title.
     *
     * @param workPackageTitle the workPackageTitle to set
     */
    public void setWorkPackageTitle(String workPackageTitle) {
        this.workPackageTitle = workPackageTitle;
    }

    /**
     * Gets unallocated budget.
     *
     * @return the unallocatedBudget
     */
    public float getUnallocatedBudget() {
        return unallocatedBudget;
    }

    /**
     * Sets unallocated budget.
     *
     * @param unallocatedBudget the unallocatedBudget to set
     */
    public void setUnallocatedBudget(float unallocatedBudget) {
        this.unallocatedBudget = unallocatedBudget;
    }

    /**
     * Gets starting budget.
     *
     * @return the starting budget
     */
    public float getStartingBudget() {
        return startingBudget;
    }

    /**
     * Sets starting budget.
     *
     * @param startingBudget the starting budget
     */
    public void setStartingBudget(float startingBudget) {
        this.startingBudget = startingBudget;
    }

    /**
     * Is chargable boolean.
     *
     * @return the chargable
     */
    public boolean isChargable() {
        return chargable;
    }

    /**
     * Sets chargable.
     *
     * @param chargable the chargable to set
     */
    public void setChargable(boolean chargable) {
        this.chargable = chargable;
    }

    /**
     * Gets workpackage start date.
     *
     * @return the workpackageStartDate
     */
    public LocalDate getWorkpackageStartDate() {
        return workpackageStartDate;
    }

    /**
     * Sets workpackage start date.
     *
     * @param workpackageStartDate the workpackageStartDate to set
     */
    public void setWorkpackageStartDate(LocalDate workpackageStartDate) {
        this.workpackageStartDate = workpackageStartDate;
    }

    /**
     * Gets workpackage end date.
     *
     * @return the workpackageEndDate
     */
    public LocalDate getWorkpackageEndDate() {
        return workpackageEndDate;
    }

    /**
     * Sets workpackage end date.
     *
     * @param workpackageEndDate the workpackageEndDate to set
     */
    public void setWorkpackageEndDate(LocalDate workpackageEndDate) {
        this.workpackageEndDate = workpackageEndDate;
    }

    /**
     * Is completed boolean.
     *
     * @return the boolean
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Sets completed.
     *
     * @param completed the completed
     */
    public void setCompleted(boolean completed) {
        if (completed) {
            setChargable(false);
        }
        this.completed = completed;
    }

    @Override
    public int compareTo(WorkPackage otherWorkPackage) {
        if (this.workpackageID.indexOf("0") < otherWorkPackage.
                workpackageID.indexOf("0")) {
            return -1;
        } else if (this.workpackageID.indexOf("0") == otherWorkPackage.
                workpackageID.indexOf("0")) {
            return 0;
        }
        return 1;
    }
}
