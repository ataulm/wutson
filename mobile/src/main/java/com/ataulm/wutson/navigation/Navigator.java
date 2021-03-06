package com.ataulm.wutson.navigation;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ColorInt;

import com.ataulm.wutson.discover.DiscoverActivity;
import com.ataulm.wutson.episodes.EpisodesUri;
import com.ataulm.wutson.episodes.SeasonEpisodeNumber;
import com.ataulm.wutson.episodes.EpisodeActivityExtras;
import com.ataulm.wutson.search.SearchActivity;
import com.ataulm.wutson.seasons.SeasonsActivity;
import com.ataulm.wutson.settings.SettingsActivity;
import com.ataulm.wutson.showdetails.ShowDetailsActivity;
import com.ataulm.wutson.shows.ShowId;

public class Navigator {

    private static final Uri BASE_URI = Uri.parse("content://com.ataulm.wutson2");
    private static final String MIME_TYPE_SHOW_ITEM = "vnd.android.cursor.item/vnd.com.ataulm.wutson.show";
    private static final String MIME_TYPE_SEASON_DIR = "vnd.android.cursor.dir/vnd.com.ataulm.wutson.season";
    private static final String MIME_TYPE_EPISODES_DIR = "vnd.android.cursor.dir/vnd.com.ataulm.wutson.episode";
    private static final Uri WUTSON_GPLUS_COMMUNITY = Uri.parse("https://plus.google.com/communities/111719082560247438789");

    private final Activity activity;

    public Navigator(Activity activity) {
        this.activity = activity;
    }

    public void toDiscover() {
        Intent intent = new Intent(activity, DiscoverActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
    }

    public void toShowDetails(ShowId showId, String showTitle, String showBackdropUri) {
        toShowDetails(showId, showTitle, showBackdropUri, 0);
    }

    public void toShowDetails(ShowId showId, String showTitle, String showBackdropUri, @ColorInt int accentColor) {
        Uri uri = BASE_URI.buildUpon()
                .appendPath("show").appendPath(showId.toString())
                .build();

        start(view(uri, MIME_TYPE_SHOW_ITEM)
                .putExtra(ShowDetailsActivity.EXTRA_SHOW_TITLE, showTitle)
                .putExtra(ShowDetailsActivity.EXTRA_SHOW_BACKDROP, showBackdropUri)
                .putExtra(ShowDetailsActivity.EXTRA_SHOW_ACCENT_COLOR, accentColor));
    }

    public void toSeason(ShowId showId, String showTitle, int seasonNumber, @ColorInt int accentColor) {
        Uri uri = BASE_URI.buildUpon()
                .appendPath("show").appendPath(showId.toString())
                .appendPath("season").appendPath(String.valueOf(seasonNumber))
                .build();

        start(view(uri, MIME_TYPE_SEASON_DIR)
                .putExtra(SeasonsActivity.EXTRA_SHOW_TITLE, showTitle)
                .putExtra(SeasonsActivity.EXTRA_SHOW_ACCENT_COLOR, accentColor));
    }

    public void toEpisodeDetails(ShowId showId, String showTitle, SeasonEpisodeNumber seasonEpisodeNumber, @ColorInt int accentColor) {
        EpisodesUri episodesUri = new EpisodesUri(showId, seasonEpisodeNumber);
        Bundle extras = new EpisodeActivityExtras(showTitle, accentColor).asBundle();
        start(view(episodesUri.buildUriUpon(BASE_URI), MIME_TYPE_EPISODES_DIR).putExtras(extras));
    }

    public void toSearch() {
        Intent intent = new Intent(activity, SearchActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
    }

    public void toSearchFor(String query) {
        Intent intent = new Intent(activity, SearchActivity.class)
                .setAction(Intent.ACTION_SEARCH)
                .putExtra(SearchManager.QUERY, query);
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
    }

    public void toSettings() {
        Intent intent = new Intent(activity, SettingsActivity.class);
        activity.startActivity(intent);
    }

    public void toHelpAndFeedback() {
        start(view(WUTSON_GPLUS_COMMUNITY));
    }

    private Intent view(Uri uri) {
        return view(uri, null);
    }

    private Intent view(Uri uri, String mimeType) {
        return new Intent(Intent.ACTION_VIEW)
                .setDataAndType(uri, mimeType);
    }

    private void start(Intent intent) {
        activity.startActivity(intent);
    }
}
