package com.linkr.models;

import java.io.Serializable;
import java.util.Objects;

/**
 * The type Work package id.
 * @author Team Linkr
 * @version 1.0
 */
public class WorkPackageId implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * the work package's ID.
     */
    private String workpackageID;

    /**
     * the project object.
     */
    private String project;

    /**
     * Instantiates a new Work package id.
     */
    public WorkPackageId() {

    }

    /**
     * Instantiates a new Work package id.
     *
     * @param workPackageID the work package id
     * @param project       the project
     */
    public WorkPackageId(String workPackageID, String project) {
        this.workpackageID = workPackageID;
        this.project = project;
    }

    /**
     * Gets workpackage id.
     *
     * @return the workpackageID
     */
    public String getWorkpackageID() {
        return workpackageID;
    }

    /**
     * Sets workpackage id.
     *
     * @param workpackageID the workpackageID to set
     */
    public void setWorkpackageID(String workpackageID) {
        this.workpackageID = workpackageID;
    }

    /**
     * Gets project id.
     *
     * @return the projectID
     */
    public String getProjectID() {
        return project;
    }

    /**
     * Sets project id.
     *
     * @param projectID the projectID to set
     */
    public void setProjectID(String projectID) {
        this.project = projectID;
    }
        
    @Override
    public int hashCode() {
        return Objects.hash(project, workpackageID);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        WorkPackageId other = (WorkPackageId) obj;
        return Objects.equals(project, other.project)
                && Objects.equals(workpackageID, other.workpackageID);
    }
}
