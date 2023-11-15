package com.linkr.models;

import java.util.List;

/**
 * The type Project report.
 * @author Team Linkr
 * @version 1.0
 */
public class ProjectReport {

    /**
     * list.
     */
    private List<ProjectWorkPackageReport> projectWorkPackageReportList;

    /**
     * name.
     */
    private String projectName;


    /**
     * Instantiates a new Project report.
     */
    public ProjectReport() {
    }

    /**
     * Instantiates a new Project report.
     *
     * @param wpList   the wp list
     * @param projName the proj name
     */
    public ProjectReport(final List<ProjectWorkPackageReport> wpList,
                         final String projName) {
        this.projectWorkPackageReportList = wpList;
        this.projectName = projName;
    }

    /**
     * Gets project work package report list.
     *
     * @return the project work package report list
     */
    public List<ProjectWorkPackageReport> getProjectWorkPackageReportList() {
        return projectWorkPackageReportList;
    }

    /**
     * Sets project work package report list.
     *
     * @param wpList the wp list
     */
    public void setProjectWorkPackageReportList(
            final List<ProjectWorkPackageReport> wpList) {
        this.projectWorkPackageReportList = wpList;
    }

    /**
     * Gets project name.
     *
     * @return the project name
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * Sets project name.
     *
     * @param projName the proj name
     */
    public void setProjectName(final String projName) {
        this.projectName = projName;
    }
}
