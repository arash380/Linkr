package com.linkr.access;

import com.linkr.models.*;

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
 * Accesses the WorkPackageEstimateToCompletion table in the database to find,
 * create, update, and delete entries.
 * @author Bryce Daynard
 * @version 1.0
 *
 */
@Stateless
public class WorkPackageEstimateToCompletionAccessor implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * The persistence context.
     */
    private @PersistenceContext(unitName = "linkr-jpa")
        EntityManager em;

    /**
     * Zero argument constructor.
     */
    public WorkPackageEstimateToCompletionAccessor() {
    }

    /**
     * Finds and returns a list of WorkPackageEstimateToCompletions based on
     * WorkPackage.
     * @param workPackage the WorkPackage to use.
     * @return list of WorkPackageEstimateToCompletions
     */
    public WorkPackageEstimateToCompletion findAllByWorkpackageId(
        WorkPackage workPackage) {
        TypedQuery<WorkPackageEstimateToCompletion>
            workPackageEstimateToCompletionList = 
            em.createQuery(
                "SELECT w FROM WorkPackageEstimateToCompletion w"
                + " WHERE w.workPackage.workpackageID = :workpackageID AND"
                + " w.workPackage.project.projectID = :projectID",
                WorkPackageEstimateToCompletion.class).
                setParameter("workpackageID", workPackage.getWorkpackageID()).
                setParameter(
                    "projectID", workPackage.getProject().getProjectID());
        if (workPackageEstimateToCompletionList.getResultList().size() < 1) {
            return null;
        }
        return workPackageEstimateToCompletionList.getSingleResult();
    }

    /**
     * Saves a WorkPackageEstimateToCompletion to the database.
     * @param workPackageEstimateToCompletion the one to save
     */
    public void persist(
        WorkPackageEstimateToCompletion workPackageEstimateToCompletion) {
        em.persist(workPackageEstimateToCompletion);
    }

    /**
     * Updates a WorkPackageEstimateToCompletion in the database.
     * @param workPackageEstimateToCompletion the one to update
     */
    public void merge(
        WorkPackageEstimateToCompletion workPackageEstimateToCompletion) {
        if (workPackageEstimateToCompletion == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        em.merge(workPackageEstimateToCompletion);
    }

    /**
     * Removes a WorkPackageEstimateToCompletion from the database.
     * @param workPackageEstimateToCompletion the one to delete
     */
    public void remove(
        WorkPackageEstimateToCompletion workPackageEstimateToCompletion) {
        em.remove(workPackageEstimateToCompletion);
    }
}
