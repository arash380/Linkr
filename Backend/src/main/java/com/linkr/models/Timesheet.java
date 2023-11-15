package com.linkr.models;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Timesheet entity.
 *
 * @author Team Linkr
 * @version 1.0
 */
@Entity
@Table(name = "timesheet")
public class Timesheet {

    /**
     * timesheet.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int timesheetID;

    /**
     * end date.
     */
    @NotNull
    private LocalDate endDate;

    /**
     * version number.
     */
    @GeneratedValue
    @NotNull
    private int versionNumber;

    /**
     * employee.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonProperty("employeeID")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "employeeID")
    @JsonIdentityReference(alwaysAsId = true)
    @JoinColumn(name = "employee_ID")
    private Employee employee;

    /**
     * approval.
     */
    private boolean managerApproval;

    /**
     * signature.
     */
    private String employeeSignature;

    /**
     * Gets timesheet id.
     *
     * @return the timesheet id
     */
    public int getTimesheetID() {
        return timesheetID;
    }

    /**
     * Sets timesheet id.
     *
     * @param timesheetID the timesheet id
     */
    public void setTimesheetID(int timesheetID) {
        this.timesheetID = timesheetID;
    }

    /**
     * Gets end date.
     *
     * @return the end date
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Sets end date.
     *
     * @param endDate the end date
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate.
                with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));

    }

    /**
     * Gets employee.
     *
     * @return the employee
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * Sets employee.
     *
     * @param employee the employee
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    /**
     * Sets employee id.
     *
     * @param employeeID the employee ID to set
     */
    public void setEmployeeID(int employeeID) {
        this.employee = new Employee();
        this.employee.setEmployeeID(employeeID);
    }

    /**
     * Gets version number.
     *
     * @return the version number
     */
    public int getVersionNumber() {
        return versionNumber;
    }

    /**
     * Sets version number.
     *
     * @param versionNumber the version number
     */
    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    /**
     * Gets employee signature.
     *
     * @return the employee signature
     */
    public String getEmployeeSignature() {
        return employeeSignature;
    }

    /**
     * Sets employee signature.
     *
     * @param employeeSignature the employee signature
     */
    public void setEmployeeSignature(String employeeSignature) {
        this.employeeSignature = employeeSignature;
    }

    /**
     * Is manager approval boolean.
     *
     * @return the boolean
     */
    public boolean isManagerApproval() {
        return managerApproval;
    }

    /**
     * Sets manager approval.
     *
     * @param managerApproval the manager approval
     */
    public void setManagerApproval(boolean managerApproval) {
        this.managerApproval = managerApproval;
    }
}
