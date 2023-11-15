package com.linkr.access;

import com.linkr.models.Employee;
import java.io.Serializable;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 * Accesses the Employee tables in the database to create, update, and delete
 * entries.
 * @author Robert Roe A00817290
 * @version 1.0
 *
 */
@Stateless
public class EmployeeAccessor implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Entity Manager.
     */
    private @PersistenceContext(unitName = "linkr-jpa") EntityManager em;
  
    /**
     * Default constructor.
     */
    public EmployeeAccessor() { }
  
    /**
     * Finds and returns an employee based on employee id.
     * @param id the employeeId to search for
     * @return employee the Employee
     */
    public Employee find(int id) {
        return em.find(Employee.class, id);
    }
  
    /**
     * Finds and returns all employees with the same supervisor.
     * @param id the supervisorId to search for
     * @return employees the Employees
     */
    public List<Employee> findBySupervisor(int id) {
        return em.createQuery(
                "SELECT e FROM Employee e WHERE e.supervisor.employeeID = :id",
                Employee.class).
            setParameter("id", id).
            getResultList();
    }
  
    /**
     * Finds and returns all employees with the same approver.
     * @param id the approverId to search for
     * @return employees the Employees
     */
    public List<Employee> findByApprover(int id) {
        return em.createQuery(
                "SELECT e FROM Employee e WHERE e.approver.employeeID = :id",
                Employee.class).
            setParameter("id", id).
            getResultList();
    }
  
    /**
     * Finds and returns all employees.
     * @return employees .
     */
    public List<Employee> findAll() {
        return em.createQuery(
            "SELECT e FROM Employee e", Employee.class).
            getResultList();
    }
  
    /**
     * Saves an employee to the database.
     * @param employee the Employee to save
     */
    public void persist(Employee employee) {
        em.persist(employee);
    }
  
    /**
     * Updates an employee in the database.
     * @param employee the Employee to update
     */
    public void merge(Employee employee) {
        em.merge(employee);
    }
  
    /**
     * Removes an employee from the database.
     * @param employee the Employee to remove
     */
    public void remove(Employee employee) {
        em.remove(employee);
    }
  
    /**
     * Detaches employee.
     * @param employee .
     */
    public void detach(Employee employee) {
        em.detach(employee);
    }
}
