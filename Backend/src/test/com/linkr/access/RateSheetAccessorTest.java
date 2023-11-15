package com.linkr.access;

import com.linkr.models.RateSheet;
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

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for Rate Sheet Accessor.
 */
@ExtendWith(ArquillianExtension.class)
public class RateSheetAccessorTest extends ArquillianAccessorTestBase {

    private final static Logger
        LOGGER = Logger.getLogger(RateSheetAccessor.class.getName());

    static RateSheet knownRateSheet;

    @BeforeEach
    public void setup() {
        knownRateSheet = new RateSheet();
    }

    @Inject
    RateSheetAccessor RateSheetAccessor;

    @Test
    public void shouldPass() {
        assertTrue(true);
    }

    @Test
    public void checkAccessorInject() {
        assertNotNull(RateSheetAccessor);
    }
}