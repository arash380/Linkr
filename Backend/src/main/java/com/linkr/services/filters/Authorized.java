package com.linkr.services.filters;

import javax.ws.rs.NameBinding;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Role Authorization Annotation Interface.
 * @author Team Linkr
 * @version 1.0
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.LOCAL_VARIABLE})
public @interface Authorized {

    /**
     * Annotation parameter that represents the authorization level of request.
     * Default is owner.
     * @return default
     */
    AuthorizationLevels levels() default AuthorizationLevels.OWNER;

    /**
     * Specific employeeID/userID to authorize.
     * @return default
     */
    int userID() default -1;
}
