package com.linkr.services.utils;

import javax.ws.rs.ext.ParamConverter;
import java.time.LocalDate;

/**
 * The type Local date converter.
 * @author Team Linkr
 * @version 1.0
 */
public class LocalDateConverter implements ParamConverter<LocalDate> {

    @Override
    public LocalDate fromString(String value) {
        if (value == null) {
            return null;
        }
        return LocalDate.parse(value);
    }

    @Override
    public String toString(LocalDate value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }

}
