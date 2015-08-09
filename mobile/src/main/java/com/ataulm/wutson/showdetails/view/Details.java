package com.ataulm.wutson.showdetails.view;

import com.ataulm.wutson.shows.Character;
import com.ataulm.wutson.shows.Show;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Details {

    private final List<Detail> details;

    public static Details from(Show show) {
        List<Detail> details = new ArrayList<>();

        details.add(new HeaderSpaceDetail());

        if (!show.getOverview().isEmpty()) {
            details.add(new OverviewDetail(show.getOverview()));
        }

        if (!show.getCast().isEmpty()) {
            details.add(new CastTitleDetail(show.getOverview()));
            for (Character character : show.getCast()) {
                details.add(new CharacterDetail(character));
            }
        }

        return new Details(details);
    }

    Details(List<Detail> details) {
        this.details = details;
    }

    public Details() {
        this.details = Collections.emptyList();
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
