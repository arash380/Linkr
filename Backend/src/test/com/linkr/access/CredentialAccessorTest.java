package com.linkr.access;

import com.linkr.models.Credentials;
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

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for for Credential Accessor.
 */
@ExtendWith(ArquillianExtension.class)
public class CredentialAccessorTest extends ArquillianAccessorTestBase {

    private final static Logger
        LOGGER = Logger.getLogger(CredentialsAccessor.class.getName());

    static Credentials knownCredentials;

    @BeforeEach
    public void setup() {
        knownCredentials = new Credentials();
        knownCredentials.setUserName("smithJ");
        knownCredentials.setEmployeeID(111);
        knownCredentials.setPassword("pass");
    }

    @Inject
    CredentialsAccessor credentialsAccessor;

    @Test
    public void shouldPass() {
        assertTrue(true);
    }

    @Test
    public void checkAccessorInject() {
        assertNotNull(credentialsAccessor);
    }

    @Test
    public void FindKnownUserByID() {
        System.out.println("ID: " + knownCredentials.getEmployeeID());
        Credentials creds =
            credentialsAccessor.find(111);
        assertNotNull(creds);
    }

    @Test
    public void FindKnownUserByUsername() {
        Credentials creds =
            credentialsAccessor.find(knownCredentials.getUserName());
        assertNotNull(creds);
    }


    @Test
    public void SearchByWrongUsername() {
        final int lowerBound = 1000;
        final int upperBound = 100000;

        int randomNum =
            ThreadLocalRandom.current().nextInt(lowerBound, upperBound);

        Credentials creds = credentialsAccessor.find("User_" + randomNum);

        assertNull(creds);
    }
}