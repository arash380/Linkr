package com.linkr.access;

import com.linkr.models.WorkPackageEstimateToCompletion;
import com.linkr.services.BeApplication;
import com.linkr.services.CredentialsResource;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests for WorkPackageEstimateToCompletion Accessor.
 */
@ExtendWith(ArquillianExtension.class)
public class WorkPackageEstimateToCompletionAccessorTest extends ArquillianAccessorTestBase {

    private final static Logger
        LOGGER = Logger.getLogger(WorkPackageEstimateToCompletionAccessor.class.getName());

    static WorkPackageEstimateToCompletion knownWorkPackageEstimateToCompletion;

    @BeforeEach
    public void setup() {
        knownWorkPackageEstimateToCompletion =
            new WorkPackageEstimateToCompletion();
        knownWorkPackageEstimateToCompletion.setProject("1232");
        knownWorkPackageEstimateToCompletion.setWorkPackage("A1100");
    }

    @Inject
    WorkPackageEstimateToCompletionAccessor
        WorkPackageEstimateToCompletionAccessor;


    @Test
    public void checkAccessorInject() {
        assertNotNull(WorkPackageEstimateToCompletionAccessor);
    }

    @Test
    public void FindKnownWPETCByWorkPackageID() {
        WorkPackageEstimateToCompletion wpetc =
            WorkPackageEstimateToCompletionAccessor.findAllByWorkpackageId(
                knownWorkPackageEstimateToCompletion.getWorkPackage());
        assertNotNull(wpetc);
        assertEquals(wpetc.getProject().getProjectID(),
            knownWorkPackageEstimateToCompletion.getProject().getProjectID());
    }
}