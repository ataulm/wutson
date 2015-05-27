package com.ataulm.wutson.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class SimpleDate {

    private static final SimpleDate UNKNOWN = new SimpleDate(null);
    private static final int EXPECTED_LENGTH = 10;
    private static final SimpleDateFormat NAMED_DAY_MONTH_DAY_YEAR = new SimpleDateFormat("EEE d MMMM yyyy", Locale.UK);

    private final Date date;

    public static SimpleDate today() {
        Date now = new Date(System.currentTimeMillis());
        return new SimpleDate(now);
    }

    public static SimpleDate from(String tmdbDate) {
        if (tmdbDate == null) {
            return UNKNOWN;
        }
        String trimmed = tmdbDate.trim();
        if (trimmed.length() != EXPECTED_LENGTH) {
            return UNKNOWN;
        }

        int year, month, dayOfMonth;
        try {
            year = Integer.parseInt(trimmed.substring(0, 4));
            month = Integer.parseInt(trimmed.substring(5, 7));
            dayOfMonth = Integer.parseInt(trimmed.substring(8, 10));
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

    @Override
    public String toString() {
        return NAMED_DAY_MONTH_DAY_YEAR.format(date);
    }

}
