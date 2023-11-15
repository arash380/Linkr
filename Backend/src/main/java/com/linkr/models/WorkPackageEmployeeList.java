package com.linkr.models;

import java.util.List;

/**
 * The type Work package employee list.
 * @author Team Linkr
 * @version 1.0
 */
public class WorkPackageEmployeeList {

    /**
     * employee id.
     */
    private List<Integer> employeeIds;

    /**
     * wp id.
     */
    private String workPackageId;

    /**
     * project id.
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
     * Gets work package id.
     *
     * @return the work package id
     */
    public String getWorkPackageId() {
        return workPackageId;
    }

    /**
     * Sets work package id.
     *
     * @param workPackageId the work package id
     */
    public void setWorkPackageId(String workPackageId) {
        this.workPackageId = workPackageId;
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
