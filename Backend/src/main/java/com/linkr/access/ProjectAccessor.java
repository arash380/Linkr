package com.linkr.access;

import com.linkr.models.Project;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

/**
 * Accesses the Project tables in the database to create, update, and delete
 * entries.
 * @author Robert Roe A0081729
 * @version 1.0
 *
 */
@Stateless
public class ProjectAccessor implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Entity Manager.
     */
    private @PersistenceContext(unitName = "linkr-jpa")
        EntityManager em;

    /**
     * Default constructor.
     */
    public ProjectAccessor() {
    }

    /**
     * Finds and returns a project by Id.
     * @param id the project id to find
     * @return the project
     */
    public Project find(String id) {
        return em.find(Project.class, id);
    }

    /**
     * Find and return a List of Projects by employee id.
     * @param empID the Employee id to use
     * @return list of projects with that employee ID
     */
    public List<Project> findByEmpID(Integer empID) {
        return em.
            createQuery(
                "SELECT p FROM Project p WHERE p.employee.employeeID = :empID",
                Project.class).setParameter("empID", empID).getResultList();
    }

    /**
     * Find and return all Projects.
     * @return list of projects
     */
    public List<Project> findAll() {
        return em.createQuery(
            "SELECT p FROM Project p", Project.class).
            getResultList();
    }

    /**
     * Saves a Project to the database.
     * @param project the Project to save
     */
    public void persist(Project project) {
        em.persist(project);
    }

    /**
     * Updates a Project in the database with a new Project.
     * @param project the Project to update
     */
    public void merge(Project project) {
        em.merge(project);
    }

    /**
     * Removes a Project from the database.
     * @param project the Project to remove
     */
    public void remove(Project project) {
        if (em.contains(project)) {
            em.remove(project);
        } else {
            Project proj =
                em.getReference(project.getClass(), project.getProjectID());
            em.remove(proj);
        }
    }

}
