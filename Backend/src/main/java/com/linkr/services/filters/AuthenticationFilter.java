package com.linkr.services.filters;

import com.linkr.services.utils.ConfigurationSet;
import com.linkr.services.utils.PersistenceUtils;
import com.linkr.services.utils.ResourceUtils;
import com.sun.security.auth.UserPrincipal;

import javax.annotation.Priority;
import javax.json.JsonObject;
import javax.security.auth.login.CredentialExpiredException;
import javax.security.auth.login.CredentialNotFoundException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.security.Principal;

/**
 * Authentication annotation implementation.
 * @author Team Linkr
 * @version 1
 */
@Authenticated
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    /**
     * Authenticates or Rejects users based on given header token.
     *
     * @param requestContext request context.
     */
    @Override
    public void filter(ContainerRequestContext requestContext) throws IllegalStateException {

        if (ResourceUtils.isDevModeOn()) {
            return;
        }

        String token = requestContext.
                getHeaderString(ConfigurationSet.getProperty(
                "TOKEN_NAME"));

        try {
            JsonObject payload = PersistenceUtils.readJwt(token);
            PersistenceUtils.checkExpired(payload);
            requestContext = createSecurityContext(requestContext, payload);
        } catch (CredentialNotFoundException | CredentialExpiredException e) {
            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED).
                            entity("Token not valid.").build());
            e.printStackTrace();
        }
    }

    private ContainerRequestContext createSecurityContext(
            ContainerRequestContext requestContext, JsonObject payload) {
        requestContext.setSecurityContext(new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return () -> String.valueOf(payload.
                        getInt(ConfigurationSet.
                                getProperty("JWT_TITLE_SUBJECT")));
            }

            @Override
            public boolean isUserInRole(String s) {
                String role = payload.
                        getString(ConfigurationSet.
                                getProperty("JWT_TITLE_ROLE"));
                return role.equalsIgnoreCase(s);
            }

            @Override
            public boolean isSecure() {
                return false;
            }

            @Override
            public String getAuthenticationScheme() {
                return null;
            }

        });

        return requestContext;
    }
}
