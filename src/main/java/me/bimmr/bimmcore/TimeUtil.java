package me.bimmr.bimmcore;

/**
 * Created by Randy on 05/27/16.
 */
public class TimeUtil {

    private static Interval year   = new Interval("Year", "Years", "Y");
    private static Interval month  = new Interval("Month", "Months", "M");
    private static Interval day    = new Interval("Day", "Days", "D");
    private static Interval hour   = new Interval("Hour", "Hours", "h");
    private static Interval minute = new Interval("Minute", "Minutes", "m");
    private static Interval second = new Interval("Second", "Seconds", "s");

    public static void setIntervalStrings(Interval year, Interval month, Interval day, Interval hour, Interval minute, Interval second) {
        TimeUtil.year = year;
        TimeUtil.month = month;
        TimeUtil.day = day;
        TimeUtil.hour = hour;
        TimeUtil.minute = minute;
        TimeUtil.second = second;
    }

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

        String exactTime = "";
        if (years != 0)
            exactTime += years + (timeFormat == TimeFormat.SHORT ? "" : " ") + getIntervalText(timeFormat, year, years) + (timeFormat == TimeFormat.SHORT ? " " : ", ");
        if (months != 0)
            exactTime += months + (timeFormat == TimeFormat.SHORT ? "" : " ") + getIntervalText(timeFormat, month, months) + (timeFormat == TimeFormat.SHORT ? " " : ", ");
        if (days != 0)
            exactTime += days + (timeFormat == TimeFormat.SHORT ? "" : " ") + getIntervalText(timeFormat, day, days) + (timeFormat == TimeFormat.SHORT ? " " : ", ");
        if (hours != 0)
            exactTime += hours + (timeFormat == TimeFormat.SHORT ? "" : " ") + getIntervalText(timeFormat, hour, hours) + (timeFormat == TimeFormat.SHORT ? " " : ", ");
        if (minutes != 0)
            exactTime += minutes + (timeFormat == TimeFormat.SHORT ? "" : " ") + getIntervalText(timeFormat, minute, minutes) + (timeFormat == TimeFormat.SHORT ? " " : ", ");

        exactTime += time + (timeFormat == TimeFormat.SHORT ? "" : " ") + getIntervalText(timeFormat, second, time);

        return exactTime;
    }

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
        timeString += years + (timeFormat == TimeFormat.SHORT ? "" : " ") + getIntervalText(timeFormat, year, years) + (timeFormat == TimeFormat.SHORT ? " " : ", ");
        timeString += months + (timeFormat == TimeFormat.SHORT ? "" : " ") + getIntervalText(timeFormat, month, months) + (timeFormat == TimeFormat.SHORT ? " " : ", ");
        timeString += days + (timeFormat == TimeFormat.SHORT ? "" : " ") + getIntervalText(timeFormat, day, days) + (timeFormat == TimeFormat.SHORT ? " " : ", ");
        timeString += hours + (timeFormat == TimeFormat.SHORT ? "" : " ") + getIntervalText(timeFormat, hour, hours) + (timeFormat == TimeFormat.SHORT ? " " : ", ");
        timeString += minutes + (timeFormat == TimeFormat.SHORT ? "" : " ") + getIntervalText(timeFormat, minute, minutes) + (timeFormat == TimeFormat.SHORT ? " " : ", ");
        timeString += time + (timeFormat == TimeFormat.SHORT ? "" : " ") + getIntervalText(timeFormat, second, time);

        return timeString;
    }

    public static enum TimeFormat {SHORT, LONG}


    public static enum TimeInterval {YEAR, MONTH, DAY, HOUR, MINUTE, SECOND}

    public static class Interval {
        private String single, plural;
        private String small;


        public Interval(String single, String plural, String small) {
            this.single = single;
            this.plural = plural;
            this.small = small;
        }
    }

}
