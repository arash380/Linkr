package com.linkr.access;

import com.linkr.models.Credentials;
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
 * Arquillian Base for Accessor Tests.
 */
@ExtendWith(ArquillianExtension.class)
public class ArquillianAccessorTestBase {

    @Deployment
    public static Archive<?> create() {

        JavaArchive jar = ShrinkWrap.create(JavaArchive.class)
            .addPackages(true, "com.linkr.models")
            .addPackages(true, "com.linkr.access")
            .addPackages(true, "com.linkr.services.utils")
            .addAsResource("config.properties", "config.properties")
            .addAsResource("test-persistence.xml",
                "META-INF/persistence.xml");

        System.out.println("Jar: \n" + jar.toString(true));

        return jar;
    }
}