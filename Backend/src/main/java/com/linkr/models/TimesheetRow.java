package com.linkr.models;

import com.fasterxml.jackson.annotation.*;
import com.linkr.services.utils.TimesheetRowTimeConverter;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * TimesheetRow entity.
 *
 * @author Team Linkr
 * @version 1.0
 */
@Entity
@IdClass(TimesheetRowId.class)
@Table(name = "timesheet_row")
public class TimesheetRow {

    /**
     * timesheet.
     */
    @Id
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonProperty("timesheetID")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "timesheetID")
    @JsonIdentityReference(alwaysAsId = true)
    @JoinColumn(name = "timesheet_ID")
    private Timesheet timesheet;

    /**
     * work package.
     */
    @Id
    @NotNull
    @ManyToOne
    @JsonProperty("workPackageID")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "workpackageID")
    @JsonIdentityReference(alwaysAsId = true)
    private WorkPackage workPackage;

    /**
     * project.
     */
    @Transient
    @JsonProperty("projectID")
    private String project;

    /**
     * hours.
     */
    @NotNull
    @JsonIgnore
    private long hoursWorked;

    /**
     * hours.
     */
    @Transient
    private float[] hoursInDays;

    /**
     * timesheet row.
     */
    @Transient
    private TimesheetRowTimeConverter timesheetRowTimeConverter;

    /**
     * Gets timesheet.
     *
     * @return the timesheet
     */
    public Timesheet getTimesheet() {
        return timesheet;
    }

    /**
     * Sets timesheet.
     *
     * @param timesheet the timesheet
     */
    public void setTimesheet(Timesheet timesheet) {
        this.timesheet = timesheet;
    }

    /**
     * Sets timesheet.
     *
     * @param timesheetID the timesheet ID to set
     */
    public void setTimesheet(String timesheetID) {
        this.timesheet = new Timesheet();
        this.timesheet.setTimesheetID(Integer.parseInt(timesheetID));
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
    public void setWorkPackage(String workPackageID) {
        this.workPackage = new WorkPackage();
        this.workPackage.setWorkpackageID(workPackageID);
        this.workPackage.setProject(this.project);
    }

    /**
     * Gets hours worked.
     *
     * @return the hoursWorked
     */
    public long getHoursWorked() {
        return hoursWorked;
    }

    /**
     * Sets hours worked.
     *
     * @param hoursWorked the hoursWorked to set
     */
    public void setHoursWorked(long hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    /**
     * Get hours in days float [ ].
     *
     * @return hoursInDays float [ ]
     */
    public float[] getHoursInDays() {
        return hoursInDays;
    }

    /**
     * Sets hours in days.
     *
     * @param hoursInDays hours as a float
     */
    public void setHoursInDays(float[] hoursInDays) {
        this.hoursInDays = hoursInDays;
    }

    /**
     * Gets project.
     *
     * @return the project
     */
    public String getProject() {
        return project;
    }

    /**
     * Sets project.
     *
     * @param project the project
     */
    public void setProject(String project) {
        this.project = project;
        if (this.getWorkPackage().getProject().getProjectID() == null) {
            this.workPackage.setProject(project);
        }
    }

    /**
     * Sets hours as float.
     */
    @PostLoad
    public void setHoursAsFloat() {
        setProject(workPackage.getProject().getProjectID());
        timesheetRowTimeConverter = new TimesheetRowTimeConverter();
        setHoursInDays(timesheetRowTimeConverter.getHours(hoursWorked));
    }

    /**
     * Sets long from hours array.
     */
    @PrePersist
    public void setLongFromHoursArray() {
        timesheetRowTimeConverter = new TimesheetRowTimeConverter();
        setHoursWorked(timesheetRowTimeConverter.setHours(hoursInDays));
    }
    
}
