package com.linkr.services.filters;

import com.linkr.services.utils.ConfigurationSet;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

/**
 * Applies CORS response context to all resource classes.
 * @author Team Linkr
 * @version 1.0
 */
@Provider
public class CorsFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext containerRequestContext,
                       ContainerResponseContext containerResponseContext) {

        containerResponseContext.getHeaders().putSingle(
            "Access-Control-Allow-Origin", "*");

        containerResponseContext.getHeaders().add(
            "Access-Control-Allow-Credentials", "true");

        containerResponseContext.getHeaders().add(
            "Access-Control-Expose-Headers", ConfigurationSet.getProperty(
                "TOKEN_NAME").toLowerCase());

        containerResponseContext.getHeaders().add(
            "Access-Control-Allow-Headers",
            "origin, content-type, accept, " + ConfigurationSet.getProperty(
                "TOKEN_NAME").toLowerCase());

        containerResponseContext.getHeaders().add(
            "Access-Control-Allow-Methods",
            "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    }
}
