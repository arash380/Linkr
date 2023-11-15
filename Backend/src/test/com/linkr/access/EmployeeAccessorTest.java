package com.linkr.access;

import com.linkr.models.Employee;
import com.linkr.services.BeApplication;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for Employee Accessor.
 */
@ExtendWith(ArquillianExtension.class)
public class EmployeeAccessorTest extends ArquillianAccessorTestBase {

    private final static Logger
        LOGGER = Logger.getLogger(EmployeeAccessor.class.getName());

    static Employee knownEmployee;

    @BeforeEach
    public void setup() {
        knownEmployee = new Employee();
        knownEmployee.setEmployeeID(111);
        knownEmployee.setFirstName("Joe");
        knownEmployee.setSupervisor(112);
        knownEmployee.setApprover(112);
    }

    @Inject
    EmployeeAccessor EmployeeAccessor;

    @Test
    public void shouldPass() {
        assertTrue(true);
    }

    @Test
    public void checkAccessorInject() {
        assertNotNull(EmployeeAccessor);
    }

    @Test
    public void FindKnownUserByID() {
        Employee emp =
            EmployeeAccessor.find(knownEmployee.getEmployeeID());
        assertNotNull(emp);
    }

    @Test
    public void FindKnownUserBySupervisor() {
        List<Employee> employees =
            EmployeeAccessor.findBySupervisor(
                knownEmployee.getSupervisor().getEmployeeID());
        assertNotNull(employees.stream()
            .filter(e -> e.getEmployeeID() == knownEmployee.getEmployeeID()));
    }

    @Test
    public void FindKnownUserByApprover() {
        List<Employee> employees =
            EmployeeAccessor
                .findByApprover(knownEmployee.getApprover().getEmployeeID());
        assertNotNull(employees.stream()
            .filter(e -> e.getEmployeeID() == knownEmployee.getEmployeeID()));
    }

}