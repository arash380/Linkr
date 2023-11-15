package com.linkr.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Credentials entity.
 *
 * @author Team Linkr
 * @version 1.0
 */
@Entity
@Table(name = "user_credential")
public class Credentials {

    /**
     * max characters.
     */
    private static final int MAX_CHARACTERS = 10;

    /**
     * employee.
     */
    @Id
    @Column(name = "employee_ID")
    private int employeeID;

    /**
     * username.
     */
    @NotNull
    @Size(max = MAX_CHARACTERS)
    @Column(columnDefinition = "tinytext")
    private String userName;

    /**
     * password.
     */
    @NotNull
    @Column(columnDefinition = "tinytext")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    /**
     * salt.
     */
    @NotNull
    @Column(columnDefinition = "tinytext")
    @JsonIgnore
    private String salt;

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
     * Gets user name.
     *
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets user name.
     *
     * @param userName the user name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets salt.
     *
     * @return the salt
     */
    public String getSalt() {
        return salt;
    }

    /**
     * Sets salt.
     *
     * @param salt the salt
     */
    public void setSalt(String salt) {
        this.salt = salt;
    }
}
