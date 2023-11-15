package com.linkr.access;

import com.linkr.models.ProjectWorkPackageEstimate;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
* Tests for ProjectWorkPackageEstimate Accessor.
 */
@ExtendWith(ArquillianExtension.class)
public class ProjectWorkPackageEstimateAccessorTest extends ArquillianAccessorTestBase {

    private final static Logger
        LOGGER = Logger.getLogger(ProjectWorkPackageEstimateAccessor.class.getName());

    static ProjectWorkPackageEstimate knownProjectWorkPackageEstimate;

    @BeforeEach
    public void setup() {
        knownProjectWorkPackageEstimate = new ProjectWorkPackageEstimate();
        knownProjectWorkPackageEstimate.setProject("1232");
        knownProjectWorkPackageEstimate.setWorkPackage("A1100");
    }

    @Inject
    ProjectWorkPackageEstimateAccessor ProjectWorkPackageEstimateAccessor;


    @Test
    public void checkAccessorInject() {
        assertNotNull(ProjectWorkPackageEstimateAccessor);
    }

    @Test
    public void FindKnownPWPEByWPID() {
        ProjectWorkPackageEstimate emp =
            ProjectWorkPackageEstimateAccessor.findAllByWorkpackageId(knownProjectWorkPackageEstimate.getWorkPackage());
        assertNotNull(emp);
        assertEquals(emp.getProject().getProjectID(), knownProjectWorkPackageEstimate.getProject().getProjectID());
    }
}