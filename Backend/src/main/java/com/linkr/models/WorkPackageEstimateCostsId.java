package com.linkr.models;

import java.io.Serializable;
import java.util.Objects;

/**
 * The type Work package estimate costs id.
 * @author Team Linkr.
 * @version 1.0
 */
public class WorkPackageEstimateCostsId implements Serializable {

    /**
     * id.
     */
    private WorkPackage workPackage;

    /**
     * Instantiates a new Work package estimate costs id.
     */
    public WorkPackageEstimateCostsId() {

    }

    /**
     * Instantiates a new Work package estimate costs id.
     *
     * @param workPackageId the work package id
     */
    public WorkPackageEstimateCostsId(WorkPackage workPackageId) {
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
        WorkPackageEstimateCostsId that = (WorkPackageEstimateCostsId) o;
        return Objects.equals(workPackage, that.workPackage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workPackage);
    }
}
