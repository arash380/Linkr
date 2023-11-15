package com.linkr.services.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 * The type Object mapper context resolver.
 * @author Team Linkr
 * @version 1.0
 */
@Provider
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

    /**
     * MAP.
     */
    private final ObjectMapper MAP;

    /**
     * Instantiates a new Object mapper context resolver.
     */
    public ObjectMapperContextResolver() {
        MAP = new ObjectMapper();
        MAP.registerModule(new JavaTimeModule());
        MAP.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        MAP.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return MAP;
    }
}
