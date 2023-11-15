package com.linkr.models;

import java.io.Serializable;
import java.util.Objects;

/**
 * The type Project work package estimate id.
 * @author Team Linkr
 * @version 1.0
 */
public class ProjectWorkPackageEstimateId implements Serializable {

    /**
     * id.
     */
    private WorkPackage workPackage;

    /**
     * Instantiates a new Project work package estimate id.
     */
    public ProjectWorkPackageEstimateId() {

    }

    /**
     * Instantiates a new Project work package estimate id.
     *
     * @param workPackageId the work package id
     */
    public ProjectWorkPackageEstimateId(WorkPackage workPackageId) {
        this.workPackage = workPackageId;
    }

    /**
     * Gets work package id.
     *
     * @return the work package id
     */
    public WorkPackage getWorkPackageId() {
        return workPackage;
    }

    /**
     * Sets work package id.
     *
     * @param workPackageId the work package id
     */
    public void setWorkPackageId(WorkPackage workPackageId) {
        this.workPackage = workPackageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProjectWorkPackageEstimateId that = (ProjectWorkPackageEstimateId) o;
        return Objects.equals(workPackage, that.workPackage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workPackage);
    }
}
