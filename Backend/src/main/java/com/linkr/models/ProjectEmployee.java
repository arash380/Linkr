package com.linkr.models;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * ProjectEmployee entity.
 * @author Team Linkr
 * @version 1.0
 */
@Entity
@IdClass(ProjectEmployeeId.class)
@Table(name = "project_employee")
public class ProjectEmployee {

    /**
     * employee.
     */
    @Id
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonProperty("employeeID")
    @JsonIdentityInfo(generator = ObjectIdGenerators.
            PropertyGenerator.class, property = "employeeID")
    @JsonIdentityReference(alwaysAsId = true)
    private Employee employee;

    /**
     * project.
     */
    @Id
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonProperty("projectID")
    @JsonIdentityInfo(generator = ObjectIdGenerators.
            PropertyGenerator.class, property = "projectID")
    @JsonIdentityReference(alwaysAsId = true)
    private Project project;

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
     * Gets project.
     *
     * @return the project
     */
    public Project getProject() {
        return project;
    }

    /**
     * Sets project.
     *
     * @param project the project
     */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * Sets employee id.
     *
     * @param employeeID the employee id
     */
    public void setEmployeeID(int employeeID) {
        this.employee = new Employee();
        this.employee.setEmployeeID(employeeID);
    }

    /**
     * Sets project id.
     *
     * @param projectID the project id
     */
    public void setProjectID(String projectID) {
        this.project = new Project();
        this.project.setProjectID(projectID);
    }
}
