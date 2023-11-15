package com.linkr.access;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;


import com.linkr.models.ProjectEmployee;
import com.linkr.models.ProjectEmployeeId;

/**
 * Accesses ProjectEmployee Tables from the database.
 * @author Edmond
 * @version 1.0
 */
@Stateless
public class ProjectEmployeeAccessor implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Entity Manager.
     */
    private @PersistenceContext(unitName = "linkr-jpa")
        EntityManager em;

    /**
     * Default constructor.
     */
    public ProjectEmployeeAccessor() {
    }

    /**
     * Finds and returns a ProjectEmployee by employeeID and projectID.
     * @param employeeId the ID of the Employee
     * @param projectId the ID of the Project
     * @return ProjectEmployee
     */
    public ProjectEmployee find(int employeeId, String projectId) {
        try {
            return em.find(ProjectEmployee.class,
                    new ProjectEmployeeId(employeeId, projectId));
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Finds and returns a list of all ProjectEmployees.
     * @return list of ProjectEmployees
     */
    public List<ProjectEmployee> findAll() {
        List<ProjectEmployee> projectEmployees = em.createQuery(
                "SELECT p FROM ProjectEmployee p",
                ProjectEmployee.class).getResultList();
        return projectEmployees;
    }
    
    /**
     * Finds and returns a list of ProjectEmployees based on EmployeeID.
     * @param employeeID the ID of the Employee to use
     * @return list of projectEmployees
     */
    public List<ProjectEmployee> findAllByEmployeeID(int employeeID) {
        List<ProjectEmployee> projectEmployees = em.createQuery(
                "SELECT p FROM ProjectEmployee p"
                + " WHERE p.employee.employeeID = :employeeID",
                ProjectEmployee.class).
                setParameter("employeeID", employeeID).
                getResultList();
        return projectEmployees;
    }
    
    /**
     * Finds and returns a list of ProjectEmployees by the ID of a Project.
     * @param projectID the ID of the Project to use
     * @return list of ProjectEmployees
     */
    public List<ProjectEmployee> findAllByProjectID(String projectID) {
        List<ProjectEmployee> projectEmployees = em.createQuery(
                "SELECT p FROM ProjectEmployee p"
                + " WHERE p.project.projectID = :projectID",
                ProjectEmployee.class).
                setParameter("projectID", projectID).
                getResultList();
        return projectEmployees;
    }

    /**
     * Saves a ProjectEmployee to the database.
     * @param projectEmployee the ProjectEmployee to save
     */
    public void persist(ProjectEmployee projectEmployee) {
        em.persist(projectEmployee);
    }

    /**
     * Removes a ProjectEmployee from the database.
     * @param projectEmployee the ProjectEmployee to remove.
     */
    public void remove(ProjectEmployee projectEmployee) {
        em.remove(projectEmployee);
    }
}
