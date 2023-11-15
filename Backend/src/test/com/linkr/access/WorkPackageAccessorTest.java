package com.linkr.access;

import com.linkr.models.WorkPackage;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;
import java.util.List;
import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for Work Package Accessor.
 */
@ExtendWith(ArquillianExtension.class)
public class WorkPackageAccessorTest extends ArquillianAccessorTestBase {

    private final static java.util.logging.Logger
        LOGGER =
        java.util.logging.Logger.getLogger(WorkPackageAccessor.class.getName());

    static WorkPackage knownWorkPackage;

    @BeforeEach
    public void setup() {
        knownWorkPackage = new WorkPackage();
        knownWorkPackage.setProject("A1100");
        knownWorkPackage.setWorkpackageID("1232");
    }

    @Inject
    WorkPackageAccessor WorkPackageAccessor;

    @Test
    public void shouldPass() {
        assertTrue(true);
    }

    @Test
    public void checkAccessorInject() {
        assertNotNull(WorkPackageAccessor);
    }

    @Test
    public void FindWorkPackageByProjectId() {
        String knownProjID = knownWorkPackage.getProject().getProjectID();
        String knownWpID = knownWorkPackage.getWorkpackageID();
        LOGGER.log(
            Level.INFO, " Work Package: {0}, Work Package: {1}",
            new Object[] {knownWpID, knownProjID});
        List<WorkPackage> workPackages =
            WorkPackageAccessor.findByProjectId(knownProjID);
        assertNotNull(workPackages);
        assertNotNull(workPackages.stream()
            .filter(wp -> wp.getProject().getProjectID().equals(knownProjID)));
    }

    @Test
    public void FindChildrenWorkPackages() {
        String knownProjID = knownWorkPackage.getProject().getProjectID();
        String knownWpID = knownWorkPackage.getWorkpackageID();

        List<WorkPackage> workPackages =
            WorkPackageAccessor
                .findChildrenWorkPackages(knownWpID, knownProjID);
        assertNotNull(workPackages);
        assertNotNull(workPackages.stream()
            .filter(wp -> wp.getProject().getProjectID().equals(knownProjID)));
    }
}