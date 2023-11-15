package com.linkr.access;

import com.linkr.models.Timesheet;
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
 * Tests for Timesheet Accessor.
 */
@ExtendWith(ArquillianExtension.class)
public class TimesheetAccessorTest extends ArquillianAccessorTestBase {

    private final static Logger
        LOGGER = Logger.getLogger(TimesheetAccessor.class.getName());

    static Timesheet knownTimesheet;

    @BeforeEach
    public void setup() {
        knownTimesheet = new Timesheet();
        knownTimesheet.setTimesheetID(1);
    }

    @Inject
    TimesheetAccessor TimesheetAccessor;

    @Test
    public void shouldPass() {
        assertTrue(true);
    }

    @Test
    public void checkAccessorInject() {
        assertNotNull(TimesheetAccessor);
    }

    @Test
    public void FindTS() {
        Timesheet emp =
            TimesheetAccessor.find(knownTimesheet.getTimesheetID());
        assertNotNull(emp);
    }
}