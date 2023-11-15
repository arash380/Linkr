package com.linkr.models;

import java.io.Serializable;
import java.util.Objects;

/**
 * The type Rate sheet id.
 * @author Team Linkr
 * @version 1.0
 */
public class RateSheetId implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * name.
     */
    private String labourRateName;

    /**
     * year.
     */
    private int year;

    /**
     * Instantiates a new Rate sheet id.
     */
    public RateSheetId() {

    }

    /**
     * Instantiates a new Rate sheet id.
     *
     * @param labourRateName the labour rate name
     * @param year           the year
     */
    public RateSheetId(String labourRateName, int year) {
        this.labourRateName = labourRateName;
        this.year = year;
    }

    /**
     * Gets labour rate name.
     *
     * @return the labour rate name
     */
    public String getLabourRateName() {
        return labourRateName;
    }

    /**
     * Sets labour rate name.
     *
     * @param labourGradeName the labour grade name
     */
    public void setLabourRateName(String labourGradeName) {
        this.labourRateName = labourGradeName;
    }

    /**
     * Gets year.
     *
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * Sets year.
     *
     * @param year the year
     */
    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RateSheetId that = (RateSheetId) o;
        return year == that.year
                && Objects.equals(labourRateName, that.labourRateName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(labourRateName, year);
    }
}
