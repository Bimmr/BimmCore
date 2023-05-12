package me.bimmr.bimmcore.utils;


/**
 * The type Time util.
 */
public class TimeUtil {

    private static Interval year   = new Interval("Year", "Years", "Y");
    private static Interval month  = new Interval("Month", "Months", "M");
    private static Interval day    = new Interval("Day", "Days", "D");
    private static Interval hour   = new Interval("Hour", "Hours", "h");
    private static Interval minute = new Interval("Minute", "Minutes", "m");
    private static Interval second = new Interval("Second", "Seconds", "s");

    /**
     * Sets interval strings.
     *
     * @param year   the year
     * @param month  the month
     * @param day    the day
     * @param hour   the hour
     * @param minute the minute
     * @param second the second
     */
    public static void setIntervalStrings(Interval year, Interval month, Interval day, Interval hour, Interval minute, Interval second) {
        TimeUtil.year = year;
        TimeUtil.month = month;
        TimeUtil.day = day;
        TimeUtil.hour = hour;
        TimeUtil.minute = minute;
        TimeUtil.second = second;
    }

    /**
     * Gets interval.
     *
     * @param timeInterval the time interval
     * @param time         the time
     * @return the interval
     */
    public static long getInterval(TimeInterval timeInterval, long time) {

        long minutes = time / 60;
        time %= 60;
        long hours = minutes / 60;
        minutes %= 60;
        long days = hours / 24;
        hours %= 24;
        long months = days / 30;
        days %= 24;
        long years = months / 12;
        months %= 24;

        switch (timeInterval) {

            case YEAR:
                return years;
            case MONTH:
                return months;
            case DAY:
                return days;
            case HOUR:
                return hours;
            case MINUTE:
                return minutes;
            case SECOND:
                return time;
            default:
                return 0;
        }
    }

    private static String getIntervalText(TimeFormat timeFormat, Interval interval, long time) {
        String value = "";
        if (timeFormat == TimeFormat.SHORT)
            value = interval.small;
        else if (time > 1)
            value = interval.plural;
        else
            value = interval.single;

        return value;
    }

    /**
     * Gets exact time.
     *
     * @param timeFormat the time format
     * @param time       the time
     * @return the exact time
     */
    public static String getExactTime(TimeFormat timeFormat, long time) {
        long minutes = time / 60;
        time %= 60;
        long hours = minutes / 60;
        minutes %= 60;
        long days = hours / 24;
        hours %= 24;
        long months = days / 30;
        days %= 24;
        long years = months / 12;
        months %= 24;

        String spaceFormat = timeFormat == TimeFormat.SHORT ? "" : " ";
        String exactTime = "";

        if (years != 0)
            exactTime += (exactTime == "" ? "" : " ") + years + spaceFormat + getIntervalText(timeFormat, year, years);
        if (months != 0)
            exactTime += (exactTime == "" ? "" : " ") + months + spaceFormat + getIntervalText(timeFormat, month, months);
        if (days != 0)
            exactTime += (exactTime == "" ? "" : " ") + days + spaceFormat + getIntervalText(timeFormat, day, days);
        if (hours != 0)
            exactTime += (exactTime == "" ? "" : " ") + hours + spaceFormat + getIntervalText(timeFormat, hour, hours);
        if (minutes != 0)
            exactTime += (exactTime == "" ? "" : " ") + minutes + spaceFormat + getIntervalText(timeFormat, minute, minutes);
        if (time != 0)
            exactTime += (exactTime == "" ? "" : " ") + time + spaceFormat + getIntervalText(timeFormat, second, time);

        return exactTime;
    }

    /**
     * Gets time.
     *
     * @param timeFormat the time format
     * @param time       the time
     * @return the time
     */
    public static String getTime(TimeFormat timeFormat, long time) {

        long minutes = time / 60;
        time %= 60;
        long hours = minutes / 60;
        minutes %= 60;
        long days = hours / 24;
        hours %= 24;
        long months = days / 30;
        days %= 24;
        long years = months / 12;
        months %= 24;

        String timeString = "";
        String spaceFormat = timeFormat == TimeFormat.SHORT ? "" : " ";

        timeString += years + spaceFormat + getIntervalText(timeFormat, year, years);
        timeString += " " + months + spaceFormat + getIntervalText(timeFormat, month, months);
        timeString += " " + days + spaceFormat + getIntervalText(timeFormat, day, days);
        timeString += " " + hours + spaceFormat + getIntervalText(timeFormat, hour, hours);
        timeString += " " + minutes + spaceFormat + getIntervalText(timeFormat, minute, minutes);
        timeString += " " + time + spaceFormat + getIntervalText(timeFormat, second, time);

        return timeString;
    }

    /**
     * The enum Time format.
     */
    public static enum TimeFormat {
        /**
         * Short time format.
         */
        SHORT,
        /**
         * Long time format.
         */
        LONG}


    /**
     * The enum Time interval.
     */
    public static enum TimeInterval {
        /**
         * Year time interval.
         */
        YEAR,
        /**
         * Month time interval.
         */
        MONTH,
        /**
         * Day time interval.
         */
        DAY,
        /**
         * Hour time interval.
         */
        HOUR,
        /**
         * Minute time interval.
         */
        MINUTE,
        /**
         * Second time interval.
         */
        SECOND
    }

    /**
     * The type Interval.
     */
    public static class Interval {
        private String single, plural;
        private String small;


        /**
         * Instantiates a new Interval.
         *
         * @param single the single
         * @param plural the plural
         * @param small  the small
         */
        public Interval(String single, String plural, String small) {
            this.single = single;
            this.plural = plural;
            this.small = small;
        }
    }

}
