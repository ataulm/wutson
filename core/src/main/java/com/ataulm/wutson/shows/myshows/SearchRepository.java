package com.ataulm.wutson.shows.myshows;

import rx.Observable;

public class SearchRepository {

    public SearchRepository() {
    }

    public Observable<SearchTvResults> searchFor(String query) {
        return Observable.empty();
    }

}
