package com.linkr.access;

import com.linkr.models.WorkPackage;
import com.linkr.models.ProjectWorkPackageEstimate;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * Accesses the ProjectWorkPackageEstimate table of the database to create,
 * update, and delete entries.
 * @author Robert Roe A00817290
 * @version 1.0
 *
 */
@Stateless
public class ProjectWorkPackageEstimateAccessor implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * The persistence context.
     */
    private @PersistenceContext(unitName = "linkr-jpa")
        EntityManager em;

    /**
     * Zero argument constructor.
     */
    public ProjectWorkPackageEstimateAccessor() {
    }

    
    /**
     * Finds and returns a list of ProjectWorkPackageEstimates based on
     * workPackage.
     * @param workPackage the WorkPackage to use
     * @return list of ProjectWorkPackageEstimates
     */
    public ProjectWorkPackageEstimate findAllByWorkpackageId(
        WorkPackage workPackage) {
        TypedQuery<ProjectWorkPackageEstimate> projectWorkPackageEstimates =
            em.createQuery(
                "SELECT w FROM ProjectWorkPackageEstimate w"
                + " WHERE w.workPackage.workpackageID = :workpackageID AND"
                + " w.workPackage.project.projectID = :projectID",
                ProjectWorkPackageEstimate.class).
                setParameter("workpackageID", workPackage.getWorkpackageID()).
                setParameter("projectID", workPackage.getProject().
                    getProjectID());
        if (projectWorkPackageEstimates.getResultList().size() < 1) {
            return null;
        }
        return projectWorkPackageEstimates.getSingleResult();
    }

    /**
     * Saves a ProjectWorkPackageEstimate to the database.
     * @param projectWorkPackageEstimate the ProjectWorkPackageEstimate to save
     */
    public void persist(ProjectWorkPackageEstimate projectWorkPackageEstimate) {
        em.persist(projectWorkPackageEstimate);
    }
    
    /**
     * Updates a ProjectWorkPackageEstimate in the database.
     * @param projectWorkPackageEstimate the updated ProjectWorkPackageEstimate
     */
    public void merge(ProjectWorkPackageEstimate projectWorkPackageEstimate) {
        if (projectWorkPackageEstimate == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        em.merge(projectWorkPackageEstimate);
    }

    /**
     * Removes a ProjectWorkPackageEstimate from the database.
     * @param projectWorkPackageEstimate the removed ProjectWorkPackageEstimate
     */
    public void remove(ProjectWorkPackageEstimate projectWorkPackageEstimate) {
        em.remove(projectWorkPackageEstimate);
    }

}
