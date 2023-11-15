package com.linkr.services.utils;

/**
 * The type Timesheet row time converter.
 * Code reused from past 3910 projects.
 * @author Bruce Link
 * @version 1.0
 */
public class TimesheetRowTimeConverter {

    /**
     * Timesheet row index for Saturday.
     */
    public static final int SAT = 0;

    /**
     * Timesheet row index for Friday.
     */
    public static final int FRI = 6;

    /**
     * decimal base in float.
     */
    public static final float BASE10 = 10.0F;

    /**
     * Day number for Friday.
     */
    public static final int FIRST_DAY = SAT;

    /**
     * Day number for Friday.
     */
    public static final int LAST_DAY = FRI;

    /** Hours in a day. */
    private static final int HOURS_IN_DAY = 24;

    /** mask for packing, unpacking hours. */
    private static final long[] MASK = {0xFFL,
            0xFF00L,
            0xFF0000L,
            0xFF000000L,
            0xFF00000000L,
            0xFF0000000000L,
            0xFF000000000000L};

    /** mask for packing, unpacking hours. */
    private static final long[] UMASK = {0xFFFFFFFFFFFFFF00L,
            0xFFFFFFFFFFFF00FFL,
            0xFFFFFFFFFF00FFFFL,
            0xFFFFFFFF00FFFFFFL,
            0xFFFFFF00FFFFFFFFL,
            0xFFFF00FFFFFFFFFFL,
            0xFF00FFFFFFFFFFFFL};

    /** 2**8. */
    private static final long BYTE_BASE = 256;

    /**
     * convert hour to decihour.  hour rounded to one fractional decimal place.
     *
     * @param hour as float
     * @return equivalent number of decihours as int
     */
    public static int toDecihour(float hour) {
        return Math.round(hour * BASE10);
    }

    /**
     * Get hours array of charges, index is day number.
     *
     * @param packedHours hours for the week
     * @return hours as array of charges
     */
    public float[] getHours(long packedHours) {
        float[] result = new float[LAST_DAY + 1];
        long check = packedHours;
        for (int i = FIRST_DAY; i <= LAST_DAY; i++) {
            result[i] = check % BYTE_BASE / BASE10;
            check /= BYTE_BASE;
        }
        return result;
    }

    /**
     * Convert hours array to packed hours and store in hours field.
     * Index of array is day of week number, starting with Saturday
     *
     * @param charges array of hours to pack (single fractional digit)
     * @return long hours
     * @throws IllegalArgumentException if charges < 0 or > 24
     */
    public long setHours(float[] charges) {
        for (float charge : charges) {
            if (charge < 0.0 || charge > HOURS_IN_DAY) {
                throw new IllegalArgumentException("charge is out of "
                        + "maximum hours in day range");
            }
        }
        long result = 0;
        for (int i = LAST_DAY; i >= FIRST_DAY; i--) {
            result = result * BYTE_BASE + toDecihour(charges[i]);
        }
        return result;
    }
}
