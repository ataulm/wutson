package com.ataulm.wutson.model;

import com.ataulm.wutson.DeveloperError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EpisodesByDate {

    private final Map<SimpleDate, Episodes> map;

    EpisodesByDate(Map<SimpleDate, Episodes> map) {
        this.map = map;
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
