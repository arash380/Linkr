package com.linkr.access;

import com.linkr.models.WorkPackage;
import com.linkr.models.WorkPackageId;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Accesses the WorkPackage table in the database to find, create, update, and
 * delete entries.
 * @author Bryce Daynard
 * @version 1.0
 *
 */
@Stateless
public class WorkPackageAccessor implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * fourth char.
     */
    private static final int FOURTH_CHAR = 3;

    /**
     * fifth char.
     */
    private static final int FIFTH_CHAR = 4;

    /**
     * Entity Manager.
     */
    private @PersistenceContext(unitName = "linkr-jpa")
        EntityManager em;

    /**
     * The last possible 0 index of a WP id.
     */
    private static final int END_INDEX = 4;

    /**
     * Default constructor.
     */
    public WorkPackageAccessor() { }

    /**
     * Finds and returns a workPackage based on WorkPackageID and ProjectID.
     * @param workPackageId the workpackageID
     * @param projectId the projectID
     * @return WorkPackage
     */
    public WorkPackage find(String workPackageId, String projectId) {
        try {
            return em.find(WorkPackage.class,
                    new WorkPackageId(workPackageId, projectId));
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * Finds and returns a list of WorkPackages based on responsibleEngineerId.
     * @param responsibleEngId the employeeID who is responsible engineer
     * @return list of WorkPackages
     */
    public List<WorkPackage> findByResponsibleEngineerId(
        String responsibleEngId) {
        List<WorkPackage> workPackages = em.createQuery(
                        "SELECT w FROM WorkPackage w"
                        + " WHERE w.responsibleEngineer.employeeID = :empId",
                        WorkPackage.class).
                setParameter("empId", Integer.parseInt(responsibleEngId)).
                getResultList();
        return workPackages;
    }

    /**
     * Finds and returns a list of WorkPackages based on ProjectId.
     * @param projectId the projectId to use
     * @return list of WorkPackages
     */
    public List<WorkPackage> findByProjectId(String projectId) {
        List<WorkPackage> workPackages = em.createQuery(
                "SELECT w FROM WorkPackage w"
                + " WHERE w.project.projectID = :projectID",
                WorkPackage.class).
                setParameter("projectID", projectId).
                getResultList();
        return workPackages;
    }

    /**
     * Finds and returns a list of all WorkPackages.
     * @return list of WorkPackages
     */
    public List<WorkPackage> findAll() {
        List<WorkPackage> workPackages = em.createQuery(
                "SELECT r FROM WorkPackage r",
                WorkPackage.class).
                getResultList();
        return workPackages;
    }


    /**
     * Gets children work packages.
     * @param projectId - the project ID.
     * @param workPackageId - the WP ID.
     * @return a list of children work packages.
     */
    public List<WorkPackage> findChildrenWorkPackages(String projectId,
                                                      String workPackageId) {
        int zeroIndex = workPackageId.indexOf("0");
        if (zeroIndex == -1) {
            List<WorkPackage> empty = new ArrayList<>();
            return empty;
        }
        String prefix = workPackageId.substring(0, zeroIndex);

        List<WorkPackage> workPackages = em.createQuery(
                "SELECT w FROM WorkPackage w WHERE w.project.projectID"
                        + " = :projectID AND w.workpackageID LIKE :prefix"
                        + " AND w.workpackageID NOT LIKE :workPackageId",
                WorkPackage.class).
                setParameter("projectID", projectId).
                setParameter("prefix", prefix + "%").
                setParameter("workPackageId", workPackageId).
                getResultList();
        List<WorkPackage> trueChildren = new ArrayList<>();
        if (zeroIndex == END_INDEX) {
            zeroIndex = -1;
        }
        for (WorkPackage wp : workPackages) {
            int childIndex = wp.getWorkpackageID().indexOf("0");
            if (childIndex == -1) {
                if (childIndex == zeroIndex) {
                    trueChildren.add(wp);
                }
            } else {
                if (childIndex - 1 == zeroIndex) {
                    trueChildren.add(wp);
                }
            }
        }
        return trueChildren;
    }

    /**
     * Gets children work packages.
     * @param projectId - the project ID.
     * @param workPackageId - the WP ID.
     * @return a list of children work packages.
     */
    public List<WorkPackage> findAllChildrenWorkPackages(String projectId,
                                                      String workPackageId) {
        int zeroIndex = workPackageId.indexOf("0");
        if (zeroIndex == -1) {
            List<WorkPackage> empty = new ArrayList<>();
            return empty;
        }
        String prefix = workPackageId.substring(0, zeroIndex);

        List<WorkPackage> workPackages = em.createQuery(
                "SELECT w FROM WorkPackage w WHERE w.project.projectID"
                        + " = :projectID AND w.workpackageID LIKE :prefix"
                        + " AND w.workpackageID NOT LIKE :workPackageId",
                WorkPackage.class).
                setParameter("projectID", projectId).
                setParameter("prefix", prefix + "%").
                setParameter("workPackageId", workPackageId).
                getResultList();
        return workPackages;
    }

    /**
     * Gets children work packages.
     * @param projectId - the project ID.
     * @param workPackageId - the WP ID.
     * @return a list of children work packages.
     */
    public List<WorkPackage> findParentWorkPackages(String projectId,
                                                      String workPackageId) {
        String prefix = workPackageId.substring(0, 1);

        List<WorkPackage> workPackages = em.createQuery(
                "SELECT w FROM WorkPackage w WHERE w.project.projectID"
                        + " = :projectID AND w.workpackageID LIKE :prefix"
                        + " AND w.workpackageID NOT LIKE :workPackageId",
                WorkPackage.class).
                setParameter("projectID", projectId).
                setParameter("prefix", prefix + "%").
                setParameter("workPackageId", workPackageId).
                getResultList();
        List<WorkPackage> parents = new ArrayList<>();

        String rootParent = prefix + "0000";
        String secondParent = workPackageId.substring(0, 2) + "000";
        String thirdParent = workPackageId.substring(0, FOURTH_CHAR) + "00";
        String fourthParent = workPackageId.substring(0, FIFTH_CHAR) + "0";


        for (WorkPackage wp : workPackages) {
            if (wp.getWorkpackageID().equals(rootParent)) {
                parents.add(wp);
            }
            if (wp.getWorkpackageID().equals(secondParent)) {
                parents.add(wp);
            }
            if (wp.getWorkpackageID().equals(thirdParent)) {
                parents.add(wp);
            }
            if (wp.getWorkpackageID().equals(fourthParent)) {
                parents.add(wp);
            }
        }
        return parents;
    }

    /**
     * Saves a WorkPackage to the database.
     * @param workPackage the WorkPackage to save
     */
    public void persist(WorkPackage workPackage) {
        em.persist(workPackage);
    }

    /**
     * Updates a WorkPackage in the database.
     * @param workPackage the WorkPackage to update
     */
    public void merge(WorkPackage workPackage) {
        if (workPackage == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        em.merge(workPackage);
    }

    /**
     * Removes a WorkPackage from the database.
     * @param workPackage the WorkPackage to remove
     */
    public void remove(WorkPackage workPackage) {
        em.remove(workPackage);
    }

}
