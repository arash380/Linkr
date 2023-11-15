package com.linkr.access;

import com.linkr.models.WorkPackage;
import com.linkr.models.WorkPackageEstimateCosts;

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
 * Accesses the WorkPackageEstimateCosts table in the database to find, create,
 * update, and delete entries.
 *
 * @author Robert Roe A00817290
 * @version 1.0
 */
@Stateless
public class WorkPackageEstimateCostsAccessor implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * The persistence context.
     */
    private @PersistenceContext(unitName = "linkr-jpa")
    EntityManager em;

    /**
     * Zero argument constructor.
     */
    public WorkPackageEstimateCostsAccessor() {
    }

    /**
     * Finds and returns a list of WorkPackageEstimateCosts based on
     * WorkPackage.
     *
     * @param workPackage the WorkPackage to use.
     * @return list of WorkPackageEstimateCosts
     */
    public WorkPackageEstimateCosts findAllByWorkpackageId(
        WorkPackage workPackage) {
        TypedQuery<WorkPackageEstimateCosts> workPackageEstimateCosts =
            em.createQuery(
                "SELECT w FROM WorkPackageEstimateCosts w"
                    + " WHERE w.workPackage.workpackageID = :workpackageID AND"
                    + " w.workPackage.project.projectID = :projectID",
                WorkPackageEstimateCosts.class).
                setParameter("workpackageID", workPackage.getWorkpackageID()).
                setParameter(
                    "projectID", workPackage.getProject().getProjectID());
        if (workPackageEstimateCosts.getResultList().size() < 1) {
            return null;
        }
        return workPackageEstimateCosts.getSingleResult();
    }

    /**
     * Saves a WorkPackageEstimateCosts to the database.
     *
     * @param workPackageEstimateCosts the WorkPackageEstimateCosts ot save
     */
    public void persist(WorkPackageEstimateCosts workPackageEstimateCosts) {
        em.persist(workPackageEstimateCosts);
    }

    /**
     * Updates a WorkPackageEstimateCosts in the database.
     *
     * @param workPackageEstimateCosts the WorkPackageEstimateCosts to update
     */
    public void merge(WorkPackageEstimateCosts workPackageEstimateCosts) {
        if (workPackageEstimateCosts == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        em.merge(workPackageEstimateCosts);
    }

    /**
     * Removes a WorkPackageEstimateCosts from the database.
     *
     * @param workPackageEstimateCosts the WorkPackageEstimateCosts to remove
     */
    public void remove(WorkPackageEstimateCosts workPackageEstimateCosts) {
        em.remove(workPackageEstimateCosts);
    }

}
