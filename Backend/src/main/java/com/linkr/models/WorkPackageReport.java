package com.linkr.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type Work package report.
 *
 * @author Team Linkr.
 * @version 1.0
 */
public class WorkPackageReport {

    /**
     * projectBudgetDays.
     */
    private float projectBudgetDays;

    /**
     * project cost of labour.
     */
    private float projectBudgetLabourCost;

    /**
     * actual days.
     */
    private float actualToDateDays;

    /**
     * actual cost.
     */
    private float actualToDateCost;

    /**
     * engineers estimated days.
     */
    private float engineersEstimatedDays;

    /**
     * engineers estimated cost.
     */
    private float engineersEstimatedCost;

    /**
     * days at complete.
     */
    private float daysEstimatedAtCompletion;

    /**
     * cost at completion.
     */
    private float costEstimatedAtCompletion;

    /**
     * variance days.
     */
    private int varianceDays;

    /**
     * variance cost.
     */
    private int varianceCost;

    /**
     * percentage complete.
     */
    private int percentageComplete;

    /**
     * work package name.
     */
    private String workPackageName;

    /**
     * id.
     */
    private String workPackageId;

    /**
     * Instantiates a new Work package report.
     */
    public WorkPackageReport() {

    }

    /**
     * Gets project budget days.
     *
     * @return the project budget days
     */
    public float getProjectBudgetDays() {
        return projectBudgetDays;
    }

    /**
     * Sets project budget days.
     *
     * @param projectBudgetDays the project budget days
     */
    public void setProjectBudgetDays(float projectBudgetDays) {
        this.projectBudgetDays = projectBudgetDays;
    }

    /**
     * Gets project budget labour cost.
     *
     * @return the project budget labour cost
     */
    public float getProjectBudgetLabourCost() {
        return projectBudgetLabourCost;
    }

    /**
     * Sets project budget labour cost.
     *
     * @param projectBudgetLabourCost the project budget labour cost
     */
    public void setProjectBudgetLabourCost(float projectBudgetLabourCost) {
        this.projectBudgetLabourCost = projectBudgetLabourCost;
    }

    /**
     * Gets actual to date days.
     *
     * @return the actual to date days
     */
    public float getActualToDateDays() {
        return actualToDateDays;
    }

    /**
     * Sets actual to date days.
     *
     * @param actualToDateDays the actual to date days
     */
    public void setActualToDateDays(float actualToDateDays) {
        this.actualToDateDays = actualToDateDays;
    }

    /**
     * Gets actual to date cost.
     *
     * @return the actual to date cost
     */
    public float getActualToDateCost() {
        return actualToDateCost;
    }

    /**
     * Sets actual to date cost.
     *
     * @param actualToDateCost the actual to date cost
     */
    public void setActualToDateCost(float actualToDateCost) {
        this.actualToDateCost = actualToDateCost;
    }

    /**
     * Gets days estimated at completion.
     *
     * @return the days estimated at completion
     */
    public float getDaysEstimatedAtCompletion() {
        return daysEstimatedAtCompletion;
    }

    /**
     * Sets days estimated at completion.
     *
     * @param daysEstimatedAtCompletion the days estimated at completion
     */
    public void setDaysEstimatedAtCompletion(float daysEstimatedAtCompletion) {
        this.daysEstimatedAtCompletion = daysEstimatedAtCompletion;
    }

    /**
     * Gets cost estimated at completion.
     *
     * @return the cost estimated at completion
     */
    public float getCostEstimatedAtCompletion() {
        return costEstimatedAtCompletion;
    }

    /**
     * Sets cost estimated at completion.
     *
     * @param costEstimatedAtCompletion the cost estimated at completion
     */
    public void setCostEstimatedAtCompletion(float costEstimatedAtCompletion) {
        this.costEstimatedAtCompletion = costEstimatedAtCompletion;
    }

    /**
     * Gets work package name.
     *
     * @return the work package name
     */
    public String getWorkPackageName() {
        return workPackageName;
    }

    /**
     * Sets work package name.
     *
     * @param workPackageName the work package name
     */
    public void setWorkPackageName(String workPackageName) {
        this.workPackageName = workPackageName;
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
     * Gets engineers estimated days.
     *
     * @return the engineers estimated days
     */
    public float getEngineersEstimatedDays() {
        return engineersEstimatedDays;
    }

    /**
     * Sets engineers estimated days.
     *
     * @param engineersEstimatedDays the engineers estimated days
     */
    public void setEngineersEstimatedDays(float engineersEstimatedDays) {
        this.engineersEstimatedDays = engineersEstimatedDays;
    }

    /**
     * Gets engineers estimated cost.
     *
     * @return the engineers estimated cost
     */
    public float getEngineersEstimatedCost() {
        return engineersEstimatedCost;
    }

    /**
     * Sets engineers estimated cost.
     *
     * @param engineersEstimatedCost the engineers estimated cost
     */
    public void setEngineersEstimatedCost(float engineersEstimatedCost) {
        this.engineersEstimatedCost = engineersEstimatedCost;
    }


    /**
     * Gets variance days.
     *
     * @return the variance days
     */
    public int getVarianceDays() {
        return varianceDays;
    }

    /**
     * Sets variance days.
     *
     * @param varianceDays the variance days
     */
    public void setVarianceDays(int varianceDays) {
        this.varianceDays = varianceDays;
    }

    /**
     * Gets variance cost.
     *
     * @return the variance cost
     */
    public int getVarianceCost() {
        return varianceCost;
    }

    /**
     * Sets variance cost.
     *
     * @param varianceCost the variance cost
     */
    public void setVarianceCost(int varianceCost) {
        this.varianceCost = varianceCost;
    }

    /**
     * Gets percentage complete.
     *
     * @return the percentage complete
     */
    public int getPercentageComplete() {
        return percentageComplete;
    }

    /**
     * Sets percentage complete.
     *
     * @param percentageComplete the percentage complete
     */
    public void setPercentageComplete(int percentageComplete) {
        this.percentageComplete = percentageComplete;
    }
}
