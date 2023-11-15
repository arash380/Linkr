package com.linkr.models;


import java.io.Serializable;
import java.util.Objects;

/**
 * The type Work package employee id.
 * @author Team Linkr.
 * @version 1.0
 */
public class WorkPackageEmployeeId implements Serializable {

    /**
     * employee.
     */
    private int employee;

    /**
     * wp.
     */
    private WorkPackage workPackage;

    /**
     * Instantiates a new Work package employee id.
     */
    public WorkPackageEmployeeId() {

    }

    /**
     * Instantiates a new Work package employee id.
     *
     * @param employee    the employee
     * @param workPackage the work package
     */
    public WorkPackageEmployeeId(int employee, WorkPackage workPackage) {
        super();
        this.employee = employee;
        this.workPackage = workPackage;
    }

    /**
     * Gets employee.
     *
     * @return the employee
     */
    public int getEmployee() {
        return employee;
    }

    /**
     * Sets employee.
     *
     * @param employee the employee
     */
    public void setEmployee(int employee) {
        this.employee = employee;
    }

    /**
     * Gets work package.
     *
     * @return the work package
     */
    public WorkPackage getWorkPackage() {
        return workPackage;
    }

    /**
     * Sets work package.
     *
     * @param workPackage the work package
     */
    public void setWorkPackage(WorkPackage workPackage) {
        this.workPackage = workPackage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WorkPackageEmployeeId that = (WorkPackageEmployeeId) o;
        return employee == that.employee
                && Objects.equals(workPackage, that.workPackage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employee, workPackage);
    }
}
