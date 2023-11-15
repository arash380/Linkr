package com.linkr.models;

import java.io.Serializable;
import java.util.Objects;

/**
 * The type Work package estimate to completion id.
 *
 * @author Team Linkr
 * @version 1.0
 */
public class WorkPackageEstimateToCompletionId implements Serializable {

    /**
     * id.
     */
    private WorkPackage workPackage;

    /**
     * Instantiates a new Work package estimate to completion.
     */
    public WorkPackageEstimateToCompletionId() {

    }

    /**
     * Instantiates a new Work package estimate to completion id.
     *
     * @param workPackage the work package
     */
    public WorkPackageEstimateToCompletionId(WorkPackage workPackage) {
        this.workPackage = workPackage;
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
        WorkPackageEstimateToCompletionId that
                = (WorkPackageEstimateToCompletionId) o;
        return Objects.equals(workPackage, that.workPackage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workPackage);
    }
}
