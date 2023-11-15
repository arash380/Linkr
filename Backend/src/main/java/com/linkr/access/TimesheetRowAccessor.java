package com.linkr.access;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.linkr.models.Timesheet;
import com.linkr.models.TimesheetRow;
import com.linkr.models.TimesheetRowId;
import com.linkr.models.WorkPackage;

/**
 * Accesses the TimesheetRow tables in the database to find, create, update,
 * and delete entries.
 * @author Robert Roe A00817290
 * @version 1.0
 */
@Stateless
public class TimesheetRowAccessor implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Entity Manager.
     */
    private @PersistenceContext(unitName = "linkr-jpa")
        EntityManager em;


    /**
     * Default constructor.
     */
    public TimesheetRowAccessor() {
    }

    /**
     * Finds and returns a TimesheetRow based on endDate,
     * employeeId, projectId, workpackageId, and versionNumber.
     *
     * @param timesheetId timesheet ID
     * @param workpackage   workPackage
     * @return timesheetRow the timesheetRow
     */
    public TimesheetRow find(int timesheetId,
                             WorkPackage workpackage) {
        try {
            return em.find(TimesheetRow.class,
                new TimesheetRowId(timesheetId, workpackage));
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * Finds and returns a list of all TimesheetRows.
     * @return list of TimesheetRows
     */
    public List<TimesheetRow> findAll() {
        List<TimesheetRow> timesheetRows = em.createQuery(
                "SELECT r FROM TimesheetRow r",
                TimesheetRow.class).
                getResultList();
        return timesheetRows;
    }

    /**
     * Finds and returns all TimesheetRows based on endDate,
     * employeeId, and versionNumber.
     *
     * @param timesheet a timesheet
     * @return timesheetRow the timesheetRow
     */
    public List<TimesheetRow> findAllByTimesheet(Timesheet timesheet) {
        List<TimesheetRow> timesheetRows = em.createQuery(
            "SELECT r FROM TimesheetRow r"
            + " WHERE r.timesheet.timesheetID = :timesheetID",
            TimesheetRow.class).
            setParameter("timesheetID", timesheet.getTimesheetID()).
            getResultList();
        return timesheetRows;
    }

    /**
     * Finds and returns all TimesheetRows based on endDate,
     * employeeId, and versionNumber.
     *
     * @param workPackage a workpackage
     * @return timesheetRow the timesheetRow
     */
    public List<TimesheetRow> findByWorkPackageAndProject(
        WorkPackage workPackage) {
        List<TimesheetRow> timesheetRows = em.createQuery(
                "SELECT r FROM TimesheetRow r"
                + " WHERE r.workPackage.workpackageID = :workpackageID AND"
                + " r.workPackage.project.projectID = :projectID",
                TimesheetRow.class).
                setParameter("workpackageID", workPackage.getWorkpackageID()).
                setParameter("projectID", workPackage.getProject().
                    getProjectID()).getResultList();
        return timesheetRows;
    }

    /**
     * Finds and returns all children workpackages TimesheetRows.
     *
     * @param workPackage a workpackage
     * @return timesheetRow the timesheetRow
     */
    public List<TimesheetRow> findWPChildrenTimesheetRows(
            WorkPackage workPackage) {
        String workPackageId = workPackage.getWorkpackageID();
        int zeroIndex = workPackageId.indexOf("0");
        String prefix;
        if (zeroIndex == -1) {
            prefix = workPackageId;
        } else {
            prefix = workPackageId.substring(0, zeroIndex);
        }
        List<TimesheetRow> timesheetRows = em.createQuery(
                "SELECT r FROM TimesheetRow r"
                        + " WHERE r.workPackage.workpackageID LIKE "
                        + ":workpackageID AND "
                        + "r.workPackage.project.projectID = :projectID",
                TimesheetRow.class).
                setParameter("workpackageID", prefix + "%").
                setParameter("projectID", workPackage.getProject().
                        getProjectID()).getResultList();
        return timesheetRows;
    }

    /**
     * Saves a TimesheetRow to the database.
     *
     * @param timesheetrow the TimesheetRow to save
     */
    public void persist(TimesheetRow timesheetrow) {
        em.persist(timesheetrow);
    }

    /**
     * Updates a TimesheetRow in the database.
     *
     * @param timesheetrow the TimesheetRow to update
     */
    public void merge(TimesheetRow timesheetrow) {
        if (timesheetrow == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        em.merge(timesheetrow);
    }

    /**
     * Removes a TimesheetRow from the database.
     *
     * @param timesheetrow the TimesheetRow to remove
     */
    public void remove(TimesheetRow timesheetrow) {
        if (em.contains(timesheetrow)) {
            em.remove(timesheetrow);
        } else {
            TimesheetRow row = em.getReference(timesheetrow.getClass(),
                    new TimesheetRowId(
                            timesheetrow.getTimesheet().getTimesheetID(),
                            timesheetrow.getWorkPackage()
                    ));
            em.remove(row);
        }
    }
}
