package com.mpedy.utils;

import org.joda.time.DateTime;

/**
 *
 * @author Matteo
 * 
 * utility per il calcolo degli intevalli temporali frequentemente utilizzati
 * 
 */
public final class IntervalBoundaries {
    
    private IntervalBoundaries() {
        
    }

    /**
     * @return the startOfHalfH
     */
    public static DateTime getStartOfHalfH(DateTime day) {
        DateTime startOfHalfH = null;
        int minuteOfHour = day.getMinuteOfHour();
        if (minuteOfHour < 30) {
            startOfHalfH = day.hourOfDay().roundFloorCopy();
        } else {
            startOfHalfH = day.hourOfDay().roundFloorCopy().plusMinutes(30);
        }
        return startOfHalfH;
    }

    /**
     * @return the endOfHalfH
     */
    public static DateTime getEndOfHalfH(DateTime day) {
        return getStartOfHalfH(day).plusMinutes(30);
    }

    /**
     * @return the startOfDay
     */
    public static DateTime getStartOfDay(DateTime day) {
        return day.withMillisOfDay(0);
    }

    /**
     * @return the endOfDay
     */
    public static DateTime getEndOfDay(DateTime day) {
        return day.millisOfDay().withMaximumValue();
    }

    /**
     * @return the startOfWeek
     */
    public static DateTime getStartOfWeek(DateTime day) {
        return getStartOfDay(day).dayOfWeek().withMinimumValue();
    }

    /**
     * @return the endOfWeek
     */
    public static DateTime getEndOfWeek(DateTime day) {
        return getStartOfDay(day).dayOfWeek().withMaximumValue();
    }

    /**
     * @return the startOfMonth
     */
    public static DateTime getStartOfMonth(DateTime day) {
        return getStartOfDay(day).dayOfMonth().withMinimumValue();
    }

    /**
     * @return the endOfMonth
     */
    public static DateTime getEndOfMonth(DateTime day) {
        return getStartOfDay(day).dayOfMonth().withMaximumValue();
    }

}