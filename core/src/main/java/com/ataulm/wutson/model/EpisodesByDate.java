package com.ataulm.wutson.model;

import com.ataulm.wutson.DeveloperError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EpisodesByDate {

    private final Map<SimpleDate, Episodes> map;

    EpisodesByDate(Map<SimpleDate, Episodes> map) {
        this.map = map;
    }

    public List<SimpleDate> getDates() {
        List<SimpleDate> keys = Arrays.asList(map.keySet().toArray(new SimpleDate[map.size()]));
        Collections.sort(keys, new Comparator<SimpleDate>() {
            @Override
            public int compare(SimpleDate lhs, SimpleDate rhs) {
                if (lhs.equals(rhs)) {
                    return 0;
                } else if (lhs.isBefore(rhs)) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
        return keys;
    }

    public Episodes get(SimpleDate date) {
        return map.get(date);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (SimpleDate simpleDate : map.keySet()) {
            builder.append(simpleDate + "\n");
            for (Episode episode : map.get(simpleDate)) {
                builder.append(episode.getShowName() + ": " + episode.getName() + "\n");
            }
        }
        return builder.toString();
    }

    public static class Builder {

        private final Map<SimpleDate, List<Episode>> map;

        public Builder() {
            map = new HashMap<>();
        }

        public void add(Episode episode) {
            SimpleDate airDate = episode.getAirDate();
            if (!map.containsKey(airDate)) {
                map.put(airDate, new ArrayList<Episode>());
            }
            map.get(airDate).add(episode);
        }

        public EpisodesByDate build() {
            if (map.isEmpty()) {
                throw DeveloperError.because("no episodes, why bother with this?");
            }
            Map<SimpleDate, Episodes> properMap = new HashMap<>();
            for (SimpleDate simpleDate : map.keySet()) {
                properMap.put(simpleDate, new Episodes(map.get(simpleDate)));
            }
            return new EpisodesByDate(properMap);
        }

    }

}
