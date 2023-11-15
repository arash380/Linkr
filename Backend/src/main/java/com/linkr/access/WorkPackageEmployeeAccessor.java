package com.linkr.access;

import com.linkr.models.WorkPackage;
import com.linkr.models.WorkPackageEmployee;
import com.linkr.models.WorkPackageEmployeeId;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

/**
 * Accesses the WorkPackageEmployee table in the database to find, create,
 * update, and delete entries.
 *
 * @author Edmond
 * @version 1.0
 */
@Stateless
public class WorkPackageEmployeeAccessor implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * the persistence context.
     */
    private @PersistenceContext(unitName = "linkr-jpa")
        EntityManager em;

    /**
     * Zero argument constructor.
     */
    public WorkPackageEmployeeAccessor() {
    }

    /**
     * Finds and returns a WorkPackageEmployee based on employeeID and
     * workPackage.
     *
     * @param employeeId  the employee ID to use
     * @param workPackage the workPackage to use
     * @return WorkPackageEmployee work package employee
     */
    public WorkPackageEmployee find(int employeeId, WorkPackage workPackage) {
        try {
            return em.find(WorkPackageEmployee.class,
                    new WorkPackageEmployeeId(employeeId, workPackage));
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Finds and returns a list of WorkPackageEmployees based on employeeID.
     *
     * @param employeeId the employee ID to use
     * @return list of WorkPackageEmployees
     */
    public List<WorkPackageEmployee> findAllByEmployeeID(int employeeId) {
        List<WorkPackageEmployee> workPackageEmployees = em.createQuery(
                "SELECT w FROM WorkPackageEmployee w"
                + " WHERE w.employee.employeeID = :employeeID",
                WorkPackageEmployee.class).
            setParameter("employeeID", employeeId).getResultList();
        return workPackageEmployees;
    }

    /**
     * Finds and returns a list of chargeable WorkPackageEmployees
     * based on employeeID.
     *
     * @param employeeId the employee ID to use
     * @return list of WorkPackageEmployees
     */
    public List<WorkPackageEmployee> findAllChargableByEmployeeID(
        int employeeId) {
        List<WorkPackageEmployee> workPackageEmployees = em.createQuery(
                "SELECT w FROM WorkPackageEmployee w"
                + " WHERE w.employee.employeeID = :employeeID AND"
                + " w.workPackage.chargable = true",
                WorkPackageEmployee.class).
            setParameter("employeeID", employeeId).getResultList();
        return workPackageEmployees;
    }

    /**
     * Finds and returns a list of WorkPackageEmployees based on WorkPackage.
     *
     * @param workPackage the WorkPackage to use
     * @return list of WorkPackageEmployees
     */
    public List<WorkPackageEmployee> findAllByWorkPackageAndProjectId(
        WorkPackage workPackage) {
        List<WorkPackageEmployee> workPackageEmployees = em.createQuery(
                "SELECT w FROM WorkPackageEmployee w"
                + " WHERE w.workPackage.workpackageID = :workpackageID AND"
                + " w.workPackage.project.projectID = :projectID",
                WorkPackageEmployee.class).
            setParameter("workpackageID", workPackage.getWorkpackageID()).
            setParameter("projectID", workPackage.getProject().getProjectID()).
            getResultList();
        return workPackageEmployees;
    }


    /**
     * Find all assigned employees list.
     *
     * @param projectId the project id
     * @return the list
     */
    public List<WorkPackageEmployee> findAllAssignedEmployees(
            String projectId) {
        List<WorkPackageEmployee> workPackageEmployees = em.createQuery(
                "SELECT w FROM WorkPackageEmployee w "
                        + "WHERE w.workPackage.project.projectID = :projectID",
                WorkPackageEmployee.class).
                setParameter("projectID", projectId).
                getResultList();
        return workPackageEmployees;
    }

    /**
     * Saves a WorkPackageEmployee to the database.
     * @param workPackageEmployee the WorkPackageEmployee to save
     */
    public void persist(WorkPackageEmployee workPackageEmployee) {
        em.persist(workPackageEmployee);
    }

    /**
     * Removes a WorkPackageEmployee from the database.
     *
     * @param workPackageEmployee the WorkPackageEmployee to save
     */
    public void remove(WorkPackageEmployee workPackageEmployee) {
        em.remove(workPackageEmployee);
    }

}
