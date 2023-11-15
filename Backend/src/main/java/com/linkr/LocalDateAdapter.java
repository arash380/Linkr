package com.linkr;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;

/**
 * Adaptor for local to be displayed on xml.
 */
public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
    /**
     * Converts string to local date.
     *
     * @param v the string
     * @return the local date
     */
    public LocalDate unmarshal(String v) {
        return LocalDate.parse(v);
    }

    /**
     * Converts local date to string.
     *
     * @param v the local date
     * @return the string
     */
    public String marshal(LocalDate v) {
        return v.toString();
    }
}