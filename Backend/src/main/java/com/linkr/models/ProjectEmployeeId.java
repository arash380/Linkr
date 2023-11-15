package com.linkr.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * The composite primary key class for connecting ProjectEmployee PKs.
 *
 * @author Team Linkr
 * @version 1.0
 */
public class ProjectEmployeeId implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * employee.
     */
    private int employee;

    /**
     * project.
     */
    private String project;


    /**
     * Instantiates a new Project employee id.
     */
    public ProjectEmployeeId() {

    }


    /**
     * Instantiates a new Project employee id.
     *
     * @param employee the employee
     * @param project  the project
     */
    public ProjectEmployeeId(int employee, String project) {
        this.employee = employee;
        this.project = project;
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
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProjectEmployeeId that = (ProjectEmployeeId) o;
        return employee == that.employee
                && Objects.equals(project, that.project);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employee, project);
    }
}
