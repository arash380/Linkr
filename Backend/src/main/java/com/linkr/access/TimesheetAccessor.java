package com.linkr.access;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.linkr.models.Timesheet;

/**
 * Accesses the Timesheet tables in the database to find, create, update, and
 * delete entries.
 * @author Robert Roe A00817290
 * @version 1.0
 */
@Stateless
public class TimesheetAccessor implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Entity Manager.
     */
    private @PersistenceContext(unitName = "linkr-jpa")
        EntityManager em;


    /**
     * Default constructor.
     */
    public TimesheetAccessor() {
    }

    /**
     * Finds and returns a Timesheet based on endDate,
     * employeeId, and versionNumber.
     *
     * @param id the timesheet ID
     * @return timesheet the Timesheet
     */
    public Timesheet find(int id) {
        try {
            return em.find(Timesheet.class, id);
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * Finds and returns all Timesheets based on endDate
     * and employeeId.
     *
     * @param endDate       a LocalDate of the end of the timesheet
     * @param employeeID    an int the employeeID
     * @param versionNumber {optional} timesheet version number
     * @return timesheets the timesheets
     */
    public List<Timesheet> findByEmpIdAndEndDateAndVersion(
        LocalDate endDate, int employeeID, Integer versionNumber) {

        String vnQueryParam = versionNumber == null ? "" 
            : " AND r.versionNumber = :versionNumber";

        TypedQuery<Timesheet> query = em.createQuery(
            "SELECT r FROM Timesheet r WHERE r.endDate = :endDate"
                + " AND r.employee.employeeID = :employeeID"
                + vnQueryParam,
            Timesheet.class).
            setParameter("endDate", endDate).
            setParameter("employeeID", employeeID);

        if (versionNumber != null) {
            query.setParameter("versionNumber", versionNumber);
        }

        return query.getResultList();
    }

    /**
     * Finds and returns all Timesheets based on employeeId.
     *
     * @param employeeID an int the employeeID
     * @return timesheets the timesheets
     */
    public List<Timesheet> findByEmpId(int employeeID) {
        List<Timesheet> timesheets = em.createQuery(
            "SELECT r FROM Timesheet r"
            + " WHERE r.employee.employeeID = :employeeID",
            Timesheet.class).
            setParameter("employeeID", employeeID).
            getResultList();
        return timesheets;
    }

    /**
     * Finds and returns a list of all Timesheets.
     * @return list od Timesheets
     */
    public List<Timesheet> findAll() {
        List<Timesheet> timesheets = em.createQuery(
            "SELECT r FROM Timesheet r",
            Timesheet.class).
            getResultList();
        return timesheets;
    }

    /**
     * Saves a Timesheet to the database.
     *
     * @param timesheet the Timesheet to save
     */
    public void persist(Timesheet timesheet) {
        em.persist(timesheet);
    }

    /**
     * Updates a Timesheet in the database.
     *
     * @param timesheet the Timesheet to update
     */
    public void merge(Timesheet timesheet) {
        if (timesheet == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        em.merge(timesheet);
    }

    /**
     * Removes a Timesheet from the database.
     *
     * @param timesheet the Timesheet to remove
     */
    public void remove(Timesheet timesheet) {
        if (em.contains(timesheet)) {
            em.remove(timesheet);
        } else {
            Timesheet tsheet = em.getReference(timesheet.getClass(),
                timesheet.getTimesheetID());
            em.remove(tsheet);
        }
    }
}
