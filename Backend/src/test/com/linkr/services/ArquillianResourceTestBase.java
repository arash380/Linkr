package com.linkr.services;

import com.linkr.access.CredentialsAccessor;
import com.linkr.services.utils.ConfigurationSet;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Base for endpoint integration test.
 */
@ExtendWith(ArquillianExtension.class)
public class ArquillianResourceTestBase {

    private final static Logger LOGGER =
        Logger.getLogger(ArquillianResourceTestBase.class.getName());

    @Deployment(testable = false)
    public static Archive<?> create() {


        WebArchive war = ShrinkWrap.create(WebArchive.class)
            .addPackages(true, "com.linkr")
            .addAsResource("config.properties", "config.properties")
            .addAsResource("test-persistence.xml",
                "META-INF/persistence.xml")
            .addClass(CredentialsResource.class)
            .addAsManifestResource(EmptyAsset.INSTANCE,
                ArchivePaths.create("beans.xml"));

        System.out.println("War: \n" + war.toString(true));

        return war;
    }

    @ArquillianResource
    protected URL base;

    protected WebTarget webTarget;

    protected Client client;

    protected final String validToken =
        ConfigurationSet.getProperty("TOKEN_VALID");

    protected final String expiredToken =
        ConfigurationSet.getProperty("TOKEN_VALID");

    protected final String tokenName = ConfigurationSet.getProperty("TOKEN_NAME");

    @Test
    public void shouldPass() {
        assertTrue(true);
    }

    @BeforeEach
    public void load() throws MalformedURLException {
        this.client = ClientBuilder.newClient();

        LOGGER.log(Level.INFO, " client: {0}, baseURL: {1}",
            new Object[] {client, base});
    }
}
