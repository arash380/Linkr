package com.linkr.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * The type Work package employee.
 * @author Team Linkr.
 * @version 1.0
 */
@Entity
@IdClass(WorkPackageEmployeeId.class)
@Table(name = "employee_work_package")
public class WorkPackageEmployee {

    /**
     * work package.
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
     * employee.
     */
    @Id
    @NotNull
    @ManyToOne
    @JsonProperty("employeeID")
    @JsonIdentityInfo(generator = ObjectIdGenerators.
            PropertyGenerator.class, property = "employeeID")
    @JsonIdentityReference(alwaysAsId = true)
    private Employee employee;

    /**
     * project.
     */
    @Transient
    @JsonProperty("projectID")
    private String project;

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
        if (workPackage.getProject() != null) {
            this.project = workPackage.getProject().getProjectID();
        }
    }

    /**
     * Sets work package.
     *
     * @param workPackageID the work package id
     */
    public void setWorkPackage(String workPackageID) {
        this.workPackage = new WorkPackage();
        this.workPackage.setWorkpackageID(workPackageID);
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
     * Sets employee.
     *
     * @param employeeID the employee id
     */
    public void setEmployee(int employeeID) {
        this.employee = new Employee();
        this.employee.setEmployeeID(employeeID);
    }

    /**
     * Gets project.
     *
     * @return the project
     */
    public String getProject() {
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
     * @param project the project
     */
    public void setProject(String project) {
        this.project = project;
        if (this.getWorkPackage() != null
                && this.workPackage.getProject() == null) {
            this.workPackage.setProject(project);
        }
    }
}
