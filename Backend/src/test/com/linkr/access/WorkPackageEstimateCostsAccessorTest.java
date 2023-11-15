package com.linkr.access;

import com.linkr.models.WorkPackageEstimateCosts;
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
import javax.ws.rs.WebApplicationException;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for WorkPackageEstimateCosts Accessor.
 */
@ExtendWith(ArquillianExtension.class)
public class WorkPackageEstimateCostsAccessorTest extends ArquillianAccessorTestBase {

    private final static Logger
        LOGGER = Logger.getLogger(WorkPackageEstimateCostsAccessor.class.getName());

    static WorkPackageEstimateCosts knownWpec;

    @BeforeEach
    public void setup() {
        knownWpec = new WorkPackageEstimateCosts();
        knownWpec.setWorkPackage("A1100");
        knownWpec.setProject("1232");
    }

    @Inject
    WorkPackageEstimateCostsAccessor WorkPackageEstimateCostsAccessor;

    @Test
    public void checkAccessorInject() {
        assertNotNull(WorkPackageEstimateCostsAccessor);
    }

    @Test
    public void FindKnownWpEstimateCostsByWpID() {
        WorkPackageEstimateCosts wpec =
            WorkPackageEstimateCostsAccessor
                .findAllByWorkpackageId(knownWpec.getWorkPackage());
        assertNotNull(wpec);
        assertEquals(wpec.getProject().getProjectID(),
            knownWpec.getProject().getProjectID());
    }
}