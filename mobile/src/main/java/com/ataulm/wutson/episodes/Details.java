package com.ataulm.wutson.episodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Details {

    private final List<Detail> details;

    public static Details empty() {
        return new Details(Collections.<Detail>emptyList());
    }

    public static Details from(Episode episode) {
        List<Detail> details = new ArrayList<>();
        details.add(new HeaderSpaceDetail());
        details.add(new NameDetail(episode.getName()));
        details.add(new EpisodeNumberDetail(episode.getSeasonEpisodeNumber()));
        details.addAll(extractOverviewFrom(episode));

        return new Details(details);
    }

    private static List<Detail> extractOverviewFrom(Episode episode) {
        if (episode.getOverview() == null || episode.getOverview().trim().isEmpty()) {
            return Collections.emptyList();
        }

        List<Detail> details = new ArrayList<>();
        String[] paragraphs = episode.getOverview().split("\n");
        for (String paragraph : paragraphs) {
            if (paragraph.trim().isEmpty()) {
                continue;
            }
            details.add(new OverviewDetail(paragraph));
        }
        return details;
    }

    Details(List<Detail> details) {
        this.details = details;
    }

    Type getType(int position) {
        return details.get(position).getType();
    }

    int size() {
        return details.size();
    }

    Detail get(int position) {
        return details.get(position);
    }

}
