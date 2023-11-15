package com.linkr.models;

import java.util.List;

/**
 * The type Project employee list.
 * @author Team Linkr
 * @version 1.0
 */
public class ProjectEmployeeList {

    /**
     * employees.
     */
    private List<Integer> employeeIds;

    /**
     * project.
     */
    private String projectId;

    /**
     * Gets employee ids.
     *
     * @return the employee ids
     */
    public List<Integer> getEmployeeIds() {
        return employeeIds;
    }

    /**
     * Sets employee ids.
     *
     * @param employeeIds the employee ids
     */
    public void setEmployeeIds(List<Integer> employeeIds) {
        this.employeeIds = employeeIds;
    }

    /**
     * Gets project id.
     *
     * @return the project id
     */
    public String getProjectId() {
        return projectId;
    }

    /**
     * Sets project id.
     *
     * @param projectId the project id
     */
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
