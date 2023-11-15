package com.linkr.services.filters;

import com.linkr.services.utils.ConfigurationSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * JUnit tests for Authentication Filter.
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthenticationFilterTest {

    private final static Logger
        LOGGER = Logger.getLogger(AuthenticationFilter.class.getName());

    @Mock
    private ContainerRequestContext requestContext;

    private static final String testTokenExpired =
        ConfigurationSet.getProperty("TOKEN_EXPIRED");

    private static final String tokenName = ConfigurationSet.getProperty(
        "TOKEN_NAME");

    @Test
    public void expiredTokenAborted() {

        LOGGER.log(
            Level.INFO, " Token Name: {0}, Token: {1}",
            new Object[] {tokenName, testTokenExpired});

        when(requestContext.getHeaderString(tokenName))
            .thenReturn(testTokenExpired);

        AuthenticationFilter authenticationFilter = new AuthenticationFilter();

        authenticationFilter.filter(requestContext);

        verify(requestContext, times(1)).abortWith(any(Response.class));
    }

}