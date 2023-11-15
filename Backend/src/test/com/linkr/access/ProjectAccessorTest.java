package com.linkr.access;

import com.linkr.models.Project;
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
 * Tests for Project Accessor.
 */
@ExtendWith(ArquillianExtension.class)
public class ProjectAccessorTest extends ArquillianAccessorTestBase {

    private final static Logger
        LOGGER = Logger.getLogger(ProjectAccessor.class.getName());

    static Project knownProject;

    @BeforeEach
    public void setup() {
        knownProject = new Project();
        knownProject.setProjectID("1232");
        knownProject.setEmployeeID(112);
    }

    @Inject
    ProjectAccessor ProjectAccessor;

    @Test
    public void shouldPass() {
        assertTrue(true);
    }

    @Test
    public void checkAccessorInject() {
        assertNotNull(ProjectAccessor);
    }

    @Test
    public void FindKnownProjectByID() {
        Project emp =
            ProjectAccessor.find(knownProject.getProjectID());
        assertNotNull(emp);
    }

    @Test
    public void FindKnownProjectByEmpID() {
        List<Project> Projects =
            ProjectAccessor
                .findByEmpID(knownProject.getEmployee().getEmployeeID());
        assertNotNull(Projects.stream()
            .filter(e -> e.getProjectID().equals(knownProject.getProjectID())));
    }
}