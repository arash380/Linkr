package com.linkr.models;

import java.util.List;

/**
 * The type Project work package report.
 * @author Team Linkr.
 * @version 1.0
 */
public class ProjectWorkPackageReport {

    /**
     * parent wp.
     */
    private WorkPackageReport parentWorkPackage;

    /**
     * child wp.
     */
    private List<WorkPackageReport> childrenWorkPackages;

    /**
     * Instantiates a new Project work package report.
     */
    public ProjectWorkPackageReport() {

    }

    /**
     * Gets parent work package.
     *
     * @return the parent work package
     */
    public WorkPackageReport getParentWorkPackage() {
        return parentWorkPackage;
    }

    /**
     * Sets parent work package.
     *
     * @param parentWorkPackage the parent work package
     */
    public void setParentWorkPackage(WorkPackageReport parentWorkPackage) {
        this.parentWorkPackage = parentWorkPackage;
    }

    /**
     * Gets children work packages.
     *
     * @return the children work packages
     */
    public List<WorkPackageReport> getChildrenWorkPackages() {
        return childrenWorkPackages;
    }

    /**
     * Sets children work packages.
     *
     * @param childrenWorkPackages the children work packages
     */
    public void setChildrenWorkPackages(
            List<WorkPackageReport> childrenWorkPackages) {
        this.childrenWorkPackages = childrenWorkPackages;
    }
}
