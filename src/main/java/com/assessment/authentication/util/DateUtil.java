package com.assessment.authentication.util;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Utility class for Date
 */
public class DateUtil {

    /**
     * returns the current date and time
     *
     * @return LocalDateTime
     */
    public static final LocalDateTime getCurrentDateAndTime() {
        return LocalDateTime.now();
    }

    /**
     * returns true if the time difference is more than 24 hours
     * else return false
     *
     * @param start
     * @param end
     * @return boolean
     */
    public static final boolean findIfDifferenceIsGreaterThan24Hours(LocalDateTime start, LocalDateTime end) {
        Duration duration = Duration.between(start, end);
        return duration.getSeconds() > (24 * 60 * 60);
    }

}
