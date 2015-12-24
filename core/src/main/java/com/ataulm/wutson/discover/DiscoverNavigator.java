package com.ataulm.wutson.discover;

import com.ataulm.wutson.shows.ShowId;

public interface DiscoverNavigator {

    void toShowDetails(ShowId showId, String showTitle, String showBackdropUri, int accentColor);

}
