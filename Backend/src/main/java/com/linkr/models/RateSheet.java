package com.linkr.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * The type Rate sheet.
 * @author Team Linkr.
 * @version 1.0
 */
@Entity
@IdClass(RateSheetId.class)
@Table(name="rate_sheet")
public class RateSheet {

    /**
     * name.
     */
    @Id
    private String labourRateName;

    /**
     * year.
     */
    @Id
    private int year;

    /**
     * rate.
     */
    private float labourRate;

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
     * @param labourRateName the labour rate name
     */
    public void setLabourRateName(String labourRateName) {
        this.labourRateName = labourRateName;
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

    /**
     * Gets labour rate.
     *
     * @return the labour rate
     */
    public float getLabourRate() {
        return labourRate;
    }

    /**
     * Sets labour rate.
     *
     * @param labourRate the labour rate
     */
    public void setLabourRate(float labourRate) {
        this.labourRate = labourRate;
    }
}
