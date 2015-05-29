package com.ataulm.wutson.repository;

import com.ataulm.wutson.model.Episode;

import java.util.Comparator;

class EpisodeDateComparator implements Comparator<Episode> {

    @Override
    public int compare(Episode lhs, Episode rhs) {
        if (lhs.getAirDate().equals(rhs.getAirDate())) {
            return 0;
        }
        if (lhs.getAirDate().isBefore(rhs.getAirDate())) {
            return -1;
        }
        return 1;
    }

}
