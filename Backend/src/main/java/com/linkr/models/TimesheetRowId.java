package com.linkr.models;

import javax.persistence.Entity;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * The composite primary key class for
 * connecting TimesheetRow PKs.
 *
 * @author Team Linkr
 * @version 1.0
 */
public class TimesheetRowId implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * timesheet.
     */
    private int timesheet;

    /**
     * work package.
     */
    private WorkPackage workPackage;


    /**
     * Instantiates a new Timesheet row id.
     */
    public TimesheetRowId() {

    }


    /**
     * Constructs a TimesheetId with weekNumber, versionNumber, employeeID
     * workpackage, and project.
     *
     * @param timesheet   a timesheet
     * @param workpackage a workpackage
     */
    public TimesheetRowId(int timesheet, WorkPackage workpackage) {
        super();
        this.timesheet = timesheet;
        this.workPackage = workpackage;
    }


    /**
     * Gets timesheet.
     *
     * @return the timesheet
     */
    public int getTimesheet() {
        return timesheet;
    }

    /**
     * Sets timesheet.
     *
     * @param timesheet the timesheet
     */
    public void setTimesheet(int timesheet) {
        this.timesheet = timesheet;
    }

    /**
     * Gets workpackage.
     *
     * @return the workpackage
     */
    public WorkPackage getWorkpackage() {
        return workPackage;
    }


    /**
     * Sets workpackage.
     *
     * @param workpackage the workpackage to set
     */
    public void setWorkpackage(WorkPackage workpackage) {
        this.workPackage = workpackage;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TimesheetRowId that = (TimesheetRowId) o;
        return timesheet == that.timesheet
                && Objects.equals(workPackage, that.workPackage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timesheet, workPackage);
    }
}
