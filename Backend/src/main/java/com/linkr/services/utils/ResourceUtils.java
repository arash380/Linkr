package com.linkr.services.utils;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * The type Resource utils.
 * @author Team Linkr
 * @version 1.0
 */
public class ResourceUtils {
    /**
     * Entity found response provider response.
     *
     * @param <T>    the type parameter
     * @param entity the entity
     * @return the response
     */
    public static <T> Response entityFoundResponseProvider(T entity) {
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(entity).build();
    }

    /**
     * Entities found response provider response.
     *
     * @param <T>      the type parameter
     * @param entities the entities
     * @return the response
     */
    public static <T> Response entitiesFoundResponseProvider(List<T> entities) {
        if (entities.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(entities).build();
    }

    /**
     * Is dev mode on boolean.
     *
     * @return the boolean
     */
    public static Boolean isDevModeOn() {
        return ConfigurationSet.getProperty("DEV_MODE").equals("ON");
    }
}
