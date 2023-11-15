package com.linkr.services.filters;

import com.linkr.models.Credentials;
import com.linkr.services.utils.ConfigurationSet;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;

import javax.json.JsonObject;
import javax.security.auth.login.CredentialExpiredException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.SecurityContext;

import java.security.Principal;
import java.security.Security;

import static org.mockito.Mockito.when;

/**
 * JUnit tests for Authorization Filter.
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthorizationFilterTest {

    @Mock
    private SecurityContext securityContext;

    @Test
    public void matchEmpID_CorrectID() {
        int empID = 1;
        when(securityContext.getUserPrincipal()).thenReturn(
            () -> Integer.toString(empID));

        Assert.assertTrue(AuthorizationFilter.matchEmpID(empID, securityContext));
    }

    @Test
    public void matchEmpID_IncorrectID() {
        int empID = 1;
        when(securityContext.getUserPrincipal()).thenReturn(
            () -> Integer.toString(empID + 1));

        Assert.assertFalse(AuthorizationFilter.matchEmpID(empID, securityContext));
    }

    @Test
    public void isAdmin_CorrectName() {
        when(securityContext.isUserInRole(AuthorizationLevels.ADMIN.name())).thenReturn(true);
        Assert.assertTrue(AuthorizationFilter.isAdmin(securityContext));
    }


}