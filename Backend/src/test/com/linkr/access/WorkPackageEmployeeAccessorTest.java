package com.linkr.access;

import com.linkr.models.WorkPackageEmployee;
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
 * Tests for WorkPackageEmployee Accessor.
 */
@ExtendWith(ArquillianExtension.class)
public class WorkPackageEmployeeAccessorTest extends ArquillianAccessorTestBase {

    private final static Logger
        LOGGER = Logger.getLogger(WorkPackageEmployeeAccessor.class.getName());

    static WorkPackageEmployee knownWorkPackageEmployee;

    @BeforeEach
    public void setup() {
        knownWorkPackageEmployee = new WorkPackageEmployee();
    }

    @Inject
    WorkPackageEmployeeAccessor WorkPackageEmployeeAccessor;

    @Test
    public void checkAccessorInject() {
        assertNotNull(WorkPackageEmployeeAccessor);
    }
}