package com.linkr.services.utils;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * JUnit tests for Configuration Set.
 *
 */
@RunWith(Arquillian.class)
public class ConfigurationSetTest {

    @Deployment
    public static Archive<?> create() {

        WebArchive war = ShrinkWrap.create(WebArchive.class)
            .addPackages(true, "com.linkr.services.filters")
            .addPackages(true, "com.linkr.services.utils")
            .addAsResource("config.properties", "config.properties");

        return war;
    }


    @Test
    public void getKnownProperty() {
        Assert.assertEquals(ConfigurationSet.getProperty("TOKEN_NAME"), "token");
    }

    @Test
    public void getUnKnownProperty() {
        Assert.assertNull(ConfigurationSet.getProperty("TOKEN_NAME_10"));
    }
}