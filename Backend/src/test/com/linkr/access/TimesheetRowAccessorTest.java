package com.linkr.access;

import com.linkr.models.TimesheetRow;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for Timesheet Row Accessor.
 */
@ExtendWith(ArquillianExtension.class)
public class TimesheetRowAccessorTest extends ArquillianAccessorTestBase {

    private final static Logger
        LOGGER = Logger.getLogger(TimesheetRowAccessor.class.getName());

    static TimesheetRow knownTimesheetRow;

    @BeforeEach
    public void setup() {
        knownTimesheetRow = new TimesheetRow();
    }

    @Inject
    TimesheetRowAccessor TimesheetRowAccessor;

    @Test
    public void shouldPass() {
        assertTrue(true);
    }

    @Test
    public void checkAccessorInject() {
        assertNotNull(TimesheetRowAccessor);
    }
}