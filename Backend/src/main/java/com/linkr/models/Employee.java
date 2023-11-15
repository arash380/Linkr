package com.linkr.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;


/**
 * Employee entity.
 *
 * @author Team Linkr
 * @version 1.0
 */
@Entity

@Table(name = "employee")
public class Employee {

    /**
     * max characters.
     */
    private static final int MAX_CHARACTERS = 50;

    /**
     * employee.
     */
    @Id
    @Column(name = "ID")
    private int employeeID;

    /**
     * name.
     */
    @NotNull
    @Size(max = MAX_CHARACTERS)
    private String firstName;

    /**
     * name.
     */
    @NotNull
    @Size(max = MAX_CHARACTERS)
    private String lastName;

    /**
     * flex.
     */
    @DecimalMin("0.0")
    private float flexTime;

    /**
     * hr.
     */
    private boolean hrEmployee;

    /**
     * rate.
     */
    @Size(min = 2, max = 2)
    private String payRate;

    /**
     * Supervisor of the {@link Employee}.
     */
    @ManyToOne
    @JsonProperty("supervisorID")
    @JsonIdentityInfo(generator = ObjectIdGenerators.
            PropertyGenerator.class, property = "employeeID")
    @JsonIdentityReference(alwaysAsId = true)
    private Employee supervisor;

    /**
     * {@link Employee}s reporting to this {@link Employee}.
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "supervisor")
    @JsonIgnore
    private Collection<Employee> supervisees;

    /**
     * Approver of the {@link Employee}.
     */
    @ManyToOne
    @JsonProperty("approverID")
    @JsonIdentityInfo(generator = ObjectIdGenerators.
            PropertyGenerator.class, property = "employeeID")
    @JsonIdentityReference(alwaysAsId = true)
    private Employee approver;

    /**
     * {@link Employee}s this employee {@link Employee} approves for.
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "approver")
    @JsonIgnore
    private Collection<Employee> approvees;

    /**
     * vacation.
     */
    @DecimalMin("0.0")
    private float vacationDays;

    /**
     * sick.
     */
    @DecimalMin("0.0")
    private float sickDays;

    /**
     * salaried.
     */
    private boolean salariedEmployee;

    /**
     * active.
     */
    private boolean isActive;

    /**
     * Gets employee id.
     *
     * @return the employee id
     */
    public int getEmployeeID() {
        return employeeID;
    }

    /**
     * Sets employee id.
     *
     * @param employeeID the employee id
     */
    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    /**
     * Gets first name.
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets first name.
     *
     * @param firstName the first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets last name.
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets last name.
     *
     * @param lastName the last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets flex time.
     *
     * @return the flex time
     */
    public float getFlexTime() {
        return flexTime;
    }

    /**
     * Sets flex time.
     *
     * @param flexTime the flex time
     */
    public void setFlexTime(float flexTime) {
        this.flexTime = flexTime;
    }

    /**
     * Is hr employee boolean.
     *
     * @return the boolean
     */
    public boolean isHrEmployee() {
        return hrEmployee;
    }

    /**
     * Sets hr employee.
     *
     * @param hrEmployee the hr employee
     */
    public void setHrEmployee(boolean hrEmployee) {
        this.hrEmployee = hrEmployee;
    }

    /**
     * Gets pay rate.
     *
     * @return the pay rate
     */
    public String getPayRate() {
        return payRate;
    }

    /**
     * Sets pay rate.
     *
     * @param payRate the pay rate
     */
    public void setPayRate(String payRate) {
        this.payRate = payRate;
    }

    /**
     * Gets supervisor.
     *
     * @return the supervisor
     */
    public Employee getSupervisor() {
        return supervisor;
    }

    /**
     * Sets supervisor.
     *
     * @param supervisorID the supervisor ID to set
     */
    public void setSupervisor(int supervisorID) {
        this.supervisor = new Employee();
        this.supervisor.employeeID = supervisorID;
    }

    /**
     * Sets supervisor.
     *
     * @param supervisor the supervisor to set
     */
    public void setSupervisor(Employee supervisor) {
        this.supervisor = supervisor;
    }

    /**
     * Gets supervisees.
     *
     * @return the supervisees
     */
    public Collection<Employee> getSupervisees() {
        return supervisees;
    }

    /**
     * Sets supervisees.
     *
     * @param supervisees the supervisees to set
     */
    public void setSupervisees(ArrayList<Employee> supervisees) {
        this.supervisees = supervisees;
    }

    /**
     * Gets approver.
     *
     * @return the approver
     */
    public Employee getApprover() {
        return approver;
    }

    /**
     * Sets approver.
     *
     * @param approverID the approver ID to set
     */
    public void setApprover(int approverID) {
        this.approver = new Employee();
        this.approver.employeeID = approverID;
    }

    /**
     * Sets approver.
     *
     * @param approver the approver to set
     */
    public void setApprover(Employee approver) {
        this.approver = approver;
    }

    /**
     * Gets approvees.
     *
     * @return the aprovees
     */
    public Collection<Employee> getApprovees() {
        return approvees;
    }

    /**
     * Sets aprovees.
     *
     * @param newApprovees the aprovees to set
     */
    public void setAprovees(ArrayList<Employee> newApprovees) {
        this.approvees = newApprovees;
    }

    /**
     * Gets vacation days.
     *
     * @return the vacation days
     */
    public float getVacationDays() {
        return vacationDays;
    }

    /**
     * Sets vacation days.
     *
     * @param vacationDays the vacation days
     */
    public void setVacationDays(float vacationDays) {
        this.vacationDays = vacationDays;
    }

    /**
     * Is salaried employee boolean.
     *
     * @return the boolean
     */
    public boolean isSalariedEmployee() {
        return salariedEmployee;
    }

    /**
     * Sets salaried employee.
     *
     * @param salariedEmployee the salaried employee
     */
    public void setSalariedEmployee(boolean salariedEmployee) {
        this.salariedEmployee = salariedEmployee;
    }

    /**
     * Is active boolean.
     *
     * @return the boolean
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Sets active.
     *
     * @param active the active
     */
    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Gets sick days.
     *
     * @return the sick days
     */
    public float getSickDays() {
        return sickDays;
    }

    /**
     * Sets sick days.
     *
     * @param sickDays the sick days
     */
    public void setSickDays(float sickDays) {
        this.sickDays = sickDays;
    }
}
