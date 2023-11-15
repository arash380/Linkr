package com.linkr.services.filters;

import com.linkr.services.utils.ConfigurationSet;
import com.linkr.services.utils.PersistenceUtils;

import javax.annotation.Priority;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.json.JsonObject;
import javax.security.auth.login.CredentialNotFoundException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.ElementType;

/**
 * Annotation implementation that handles authorization for paths.
 *
 * @author Team Linkr
 * @version 1.0
 */
@Authorized
@Authenticated
@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {

    /**
     * Holds info about annotation used.
     */
    @Context
    private ResourceInfo resourceInfo;

    /**
     * Filters out access based on authorization level.
     *
     * @param requestContext request context.
     */
    @Override
    public void filter(ContainerRequestContext requestContext) {

        String token =
            requestContext.getHeaderString(ConfigurationSet.getProperty(
                "TOKEN_NAME"));

        try {
            JsonObject jsonObject = PersistenceUtils.readJwt(token);

            Authorized authorizedAnnotation = resourceInfo.getResourceMethod().
                    getAnnotation(Authorized.class);

            AuthorizationLevels role = AuthorizationLevels.valueOf(jsonObject.
                    getString(ConfigurationSet.getProperty("JWT_TITLE_ROLE")));

            if (!authorizedAnnotation.levels().equals(role)
                    && role != AuthorizationLevels.ADMIN) {
                requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED).build());
            }

        } catch (CredentialNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Match emp id boolean.
     *
     * @param empID           the emp id
     * @param securityContext the security context
     * @return the boolean
     */
    public static boolean matchEmpID(int empID,
                                     SecurityContext securityContext) {
        int jwtEmpId =
            Integer.parseInt(securityContext.getUserPrincipal().getName());
        return jwtEmpId == empID;
    }

    /**
     * Is admin boolean.
     *
     * @param securityContext the security context
     * @return the boolean
     */
    public static boolean isAdmin(SecurityContext securityContext) {
        return securityContext.isUserInRole(AuthorizationLevels.ADMIN.name());
    }

    /**
     * Match emp id or admin boolean.
     *
     * @param empID           the emp id
     * @param securityContext the security context
     * @return the boolean
     */
    public static boolean matchEmpIdOrAdmin(int empID,
                                            SecurityContext securityContext) {
        return matchEmpID(empID, securityContext)
                || isAdmin(securityContext);
    }
}
