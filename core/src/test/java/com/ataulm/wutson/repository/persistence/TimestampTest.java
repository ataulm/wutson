package com.ataulm.wutson.repository.persistence;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class TimestampTest {

    private static final long MILLIS_13_SEPTEMBER_2015 = 1442172678000l;
    private static final long MILLIS_14_SEPTEMBER_2015 = 1442259078000l;
    private static final long MILLIS_16_SEPTEMBER_2015 = 1442431878000l;

    @Test
    public void differenceInHoursForOneDayLater() {
        Timestamp base = new Timestamp(MILLIS_13_SEPTEMBER_2015);
        Timestamp other = new Timestamp(MILLIS_14_SEPTEMBER_2015);

        long hours = base.differenceInHours(other);

        assertThat(hours).isEqualTo(24);
    }

    @Test
    public void differenceInHoursForThreeDaysLater() {
        Timestamp base = new Timestamp(MILLIS_13_SEPTEMBER_2015);
        Timestamp other = new Timestamp(MILLIS_16_SEPTEMBER_2015);

        long hours = base.differenceInHours(other);

        assertThat(hours).isEqualTo(72);
    }

    @Test
    public void differenceInHoursForOneDayBefore() {
        Timestamp base = new Timestamp(MILLIS_14_SEPTEMBER_2015);
        Timestamp other = new Timestamp(MILLIS_13_SEPTEMBER_2015);

        long hours = base.differenceInHours(other);

        assertThat(hours).isEqualTo(24);
    }

}
