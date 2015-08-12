package com.ataulm.wutson.shows;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class SimpleDate {

    private static final SimpleDate UNKNOWN = SimpleDate.from("0001-01-01");
    private static final SimpleDateFormat NAMED_DAY_MONTH_DAY_YEAR = new SimpleDateFormat("EEE d MMMM yyyy", Locale.UK);

    private final Date date;

    public static SimpleDate today() {
        Date now = new Date(System.currentTimeMillis());
        return new SimpleDate(now);
    }

    public static SimpleDate from(String traktDate) {
        if (traktDate == null) {
            return UNKNOWN;
        }
        int year, month, dayOfMonth;
        try {
            year = Integer.parseInt(traktDate.substring(0, 4));
            month = Integer.parseInt(traktDate.substring(5, 7));
            dayOfMonth = Integer.parseInt(traktDate.substring(8, 10));
        } catch (NumberFormatException e) {
            return UNKNOWN;
        }

        Calendar instance = Calendar.getInstance();
        instance.set(year, month, dayOfMonth);
        Date date = new Date(instance.getTimeInMillis());
        return new SimpleDate(date);
    }

    private SimpleDate(Date date) {
        this.date = date;
    }

    public boolean isValid() {
        return !equals(UNKNOWN);
    }

    @Override
    public String toString() {
        return NAMED_DAY_MONTH_DAY_YEAR.format(date);
    }

    public boolean isBefore(SimpleDate other) {
        return (date.before(other.date));
    }

    public boolean isAfter(SimpleDate other) {
        return (date.after(other.date));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimpleDate that = (SimpleDate) o;
        return date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return date != null ? date.hashCode() : 0;
    }

}
