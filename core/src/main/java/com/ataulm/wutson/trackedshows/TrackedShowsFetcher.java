package com.ataulm.wutson.trackedshows;

import com.ataulm.wutson.AsyncFetcher;
import com.ataulm.wutson.Fetcher;
import com.ataulm.wutson.tmdb.discovertv.DiscoverTv;
import com.ataulm.wutson.tmdb.discovertv.DiscoverTvJsonParser;
import com.ataulm.wutson.tmdb.discovertv.MockDiscoverTv;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class TrackedShowsFetcher implements Fetcher<TrackedShows>, AsyncFetcher<TrackedShows> {

    @Override
    public TrackedShows fetch() {
        DiscoverTvJsonParser parser = DiscoverTvJsonParser.newInstance();
        DiscoverTv discoverTv = parser.parse(MockDiscoverTv.JSON);

        List<DiscoverTv.Show> discoverTvShows = discoverTv.getShows();
        List<TrackedShow> listOfTrackedShows = new ArrayList<>(discoverTvShows.size());

        for (DiscoverTv.Show discoverTvShow : discoverTvShows) {
            // TODO: this unknown is not cool. Fetcher<T> should aggregate data to return a complete T
            String nextEpisodeName = "Unknown";
            listOfTrackedShows.add(TrackedShow.from(discoverTvShow, nextEpisodeName));
        }

        return new TrackedShows(listOfTrackedShows);
    }

    @Override
    public Observable<TrackedShows> fetchAsync() {
        return Observable.create(new Observable.OnSubscribe<TrackedShows>() {

            @Override
            public void call(Subscriber<? super TrackedShows> subscriber) {
                TrackedShows shows = fetch();
                subscriber.onNext(shows);
                subscriber.onCompleted();
            }

        }).subscribeOn(Schedulers.io());
    }

}
