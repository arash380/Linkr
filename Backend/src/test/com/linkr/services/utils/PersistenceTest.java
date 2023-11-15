package com.linkr.services.utils;

import com.linkr.models.Credentials;
import com.linkr.services.filters.AuthenticationFilter;
import com.linkr.services.filters.AuthorizationLevels;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.json.JsonObject;
import javax.security.auth.login.CredentialNotFoundException;
import javax.ws.rs.container.ContainerRequestContext;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PersistenceTest  {

    Credentials credentials;
    private static String testTokenExpired =
        ConfigurationSet.getProperty("TOKEN_EXPIRED");

//    private static String testTokenValid =
//        ConfigurationSet.getProperty("TOKEN_VALID");

    @Before
    public void setup() {
        credentials = new Credentials();
        credentials.setEmployeeID(0);
        credentials.setUserName("user");
        credentials.setPassword("pass");
    }
    @Test
    public void createJwt() throws CredentialNotFoundException {
        AuthorizationLevels empRole = AuthorizationLevels.ADMIN;
        String jwt = PersistenceUtils.createJwt(credentials,
            AuthorizationLevels.ADMIN);
        JsonObject json = PersistenceUtils.readJwt(jwt);
        int empID = json.getJsonNumber(ConfigurationSet.getProperty("JWT_TITLE_SUBJECT")).intValue();
        String name = json.getJsonString(ConfigurationSet.getProperty("JWT_TITLE_NAME")).getString();
        String role = json.getJsonString(ConfigurationSet.getProperty("JWT_TITLE_ROLE")).getString();
        long expiry = json.getJsonNumber(ConfigurationSet.getProperty("JWT_TITLE_EXPIRY")).longValue();

        Assert.assertEquals(credentials.getEmployeeID(), empID);
        Assert.assertEquals(credentials.getUserName(), name);
        Assert.assertEquals(empRole.name(), role);
    }
}