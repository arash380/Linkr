package com.linkr.access;

import com.linkr.models.RateSheet;
import com.linkr.models.RateSheetId;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

/**
 * Accesses the RateSheet table of the database to Find ratesheet entries.
 * @author Bryce Daynard
 * @version 1.0
 *
 */
@Stateless
public class RateSheetAccessor implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The persistence context.
     */
    private @PersistenceContext(unitName = "linkr-jpa")
        EntityManager em;

    /**
     * Zero argument constructor.
     */
    public RateSheetAccessor() {
    }

    /**
     * Finds and returns a rate as a float by labourgrade and year.
     * @param labourGradeName the labourgrade
     * @param year the year
     * @return Float the rate
     */
    public Float find(String labourGradeName, int year) {
        try {
            return em.find(RateSheet.class,
                    new RateSheetId(labourGradeName, year)).getLabourRate();
        } catch (NoResultException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
