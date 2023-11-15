package com.linkr.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 * Project entity.
 * @author Team Linkr
 * @version 1.0
 */
@Entity
@Table(name = "project")
public class Project {

    /**
     * project.
     */
    @Id
    @Column(name = "ID")
    private String projectID;

    /**
     * employee.
     */
    @NotNull
    @ManyToOne
    @JsonProperty("employeeID")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "employeeID")
    @JsonIdentityReference(alwaysAsId = true)
    private Employee employee;

    /**
     * pma.
     */
    @ManyToOne
    @JsonProperty("assistantID")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "employeeID")
    @JsonIdentityReference(alwaysAsId = true)
    private Employee projectManagerAssistant;

    /**
     * active.
     */
    private boolean activeProject;

    /**
     * name.
     */
    private String projectName;

    /**
     * start.
     */
    @Temporal(TemporalType.DATE)
    private Date projectStart;

    /**
     * end.
     */
    @Temporal(TemporalType.DATE)
    private Date projectEnd;

    /**
     * budget.
     */
    private float projectBudget;

    /**
     * budget.
     */
    private float unallocatedBudget;

    /**
     * mark up.
     */
    private float projectMarkup;

    /**
     * Gets project id.
     *
     * @return the project id
     */
    public String getProjectID() {
        return projectID;
    }

    /**
     * Sets project id.
     *
     * @param projectID the project id
     */
    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    /**
     * Gets employee.
     *
     * @return the employee
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * Sets employee.
     *
     * @param manager the manager
     */
    public void setEmployee(Employee manager) {
        this.employee = manager;
    }

    /**
     * Sets employee id.
     *
     * @param employeeID the employee ID to set
     */
    public void setEmployeeID(int employeeID) {
        this.employee = new Employee();
        this.employee.setEmployeeID(employeeID);
    }

    /**
     * Sets assistant id.
     *
     * @param assistantID the assistant id
     */
    public void setAssistantID(int assistantID) {
        this.projectManagerAssistant = new Employee();
        this.projectManagerAssistant.setEmployeeID(assistantID);
    }

    /**
     * Is active project boolean.
     *
     * @return the boolean
     */
    public boolean isActiveProject() {
        return activeProject;
    }

    /**
     * Sets active project.
     *
     * @param activeProject the active project
     */
    public void setActiveProject(boolean activeProject) {
        this.activeProject = activeProject;
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
     * @param projectName the project name
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * Gets project start.
     *
     * @return the project start
     */
    public Date getProjectStart() {
        return projectStart;
    }

    /**
     * Sets project start.
     *
     * @param projectStart the project start
     */
    public void setProjectStart(Date projectStart) {
        this.projectStart = projectStart;
    }

    /**
     * Gets project end.
     *
     * @return the project end
     */
    public Date getProjectEnd() {
        return projectEnd;
    }

    /**
     * Sets project end.
     *
     * @param projectEnd the project end
     */
    public void setProjectEnd(Date projectEnd) {
        this.projectEnd = projectEnd;
    }

    /**
     * Gets project budget.
     *
     * @return the project budget
     */
    public float getProjectBudget() {
        return projectBudget;
    }

    /**
     * Sets project budget.
     *
     * @param projectBudget the project budget
     */
    public void setProjectBudget(float projectBudget) {
        this.projectBudget = projectBudget;
    }

    /**
     * Gets project manager assistant.
     *
     * @return the project manager assistant
     */
    public Employee getProjectManagerAssistant() {
        return projectManagerAssistant;
    }

    /**
     * Sets project manager assistant.
     *
     * @param projectManagerAssistant the project manager assistant
     */
    public void setProjectManagerAssistant(Employee projectManagerAssistant) {
        this.projectManagerAssistant = projectManagerAssistant;
    }

    /**
     * Gets unallocated budget.
     *
     * @return the unallocated budget
     */
    public float getUnallocatedBudget() {
        return unallocatedBudget;
    }

    /**
     * Sets unallocated budget.
     *
     * @param unallocatedBudget the unallocated budget
     */
    public void setUnallocatedBudget(float unallocatedBudget) {
        this.unallocatedBudget = unallocatedBudget;
    }

    /**
     * Gets project markup.
     *
     * @return the project markup
     */
    public float getProjectMarkup() {
        return projectMarkup;
    }

    /**
     * Sets project markup.
     *
     * @param projectMarkup the project markup
     */
    public void setProjectMarkup(float projectMarkup) {
        this.projectMarkup = projectMarkup;
    }
}
