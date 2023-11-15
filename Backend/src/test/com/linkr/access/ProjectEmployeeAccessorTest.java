package com.linkr.access;

import com.linkr.models.ProjectEmployee;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for Project Employee Accessor
 */
@ExtendWith(ArquillianExtension.class)
public class ProjectEmployeeAccessorTest extends ArquillianAccessorTestBase {

    private final static Logger
        LOGGER = Logger.getLogger(ProjectEmployeeAccessor.class.getName());

    static ProjectEmployee knownProjectEmployee;

    @BeforeEach
    public void setup() {
        knownProjectEmployee = new ProjectEmployee();
        knownProjectEmployee.setEmployeeID(111);
        knownProjectEmployee.setProjectID("1232");
    }

    @Inject
    ProjectEmployeeAccessor ProjectEmployeeAccessor;

    @Test
    public void shouldPass() {
        assertTrue(true);
    }

    @Test
    public void checkAccessorInject() {
        assertNotNull(ProjectEmployeeAccessor);
    }

    @Test
    public void FindKnownProjectEmployeeByEmpID() {
        int empId = knownProjectEmployee.getEmployee().getEmployeeID();
        List<ProjectEmployee> projectEmployees =
            ProjectEmployeeAccessor.findAllByEmployeeID(empId);
        assertNotNull(projectEmployees.stream()
            .filter(pe -> pe.getEmployee().getEmployeeID() == empId));
    }

    @Test
    public void FindKnownProjectEmployee() {
        int empId = knownProjectEmployee.getEmployee().getEmployeeID();
        String projId = knownProjectEmployee.getProject().getProjectID();
        ProjectEmployee projectEmployee =
            ProjectEmployeeAccessor.find(empId, projId);
        assertNotNull(projectEmployee);
        assertEquals(projectEmployee.getEmployee().getEmployeeID(), empId);
    }

    @Test
    public void FindKnownProjectEmployeeByProjID() {
        String projId = knownProjectEmployee.getProject().getProjectID();
        List<ProjectEmployee> projectEmployees =
            ProjectEmployeeAccessor.findAllByProjectID(projId);
        assertNotNull(projectEmployees.stream()
            .filter(pe -> pe.getProject().getProjectID().equals(projId)));
    }

}