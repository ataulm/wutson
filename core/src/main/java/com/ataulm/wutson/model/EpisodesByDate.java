package com.ataulm.wutson.model;

import com.ataulm.wutson.DeveloperError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EpisodesByDate {

    private final Map<SimpleDate, Episodes> map;

    EpisodesByDate(Map<SimpleDate, Episodes> map) {
        this.map = map;
    }

    public Set<SimpleDate> getDates() {
        return map.keySet();
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
