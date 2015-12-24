package com.ataulm.wutson.discover;

import com.ataulm.wutson.jabber.Jabber;
import com.ataulm.wutson.rx.LoggingObserver;
import com.ataulm.wutson.shows.discover.DiscoverShows;

class DiscoverShowsObserver extends LoggingObserver<DiscoverShows> {

    private final Presenter presenter;

    DiscoverShowsObserver(Presenter presenter) {
        super(Jabber.log());
        this.presenter = presenter;
    }

    @Override
    public void onNext(DiscoverShows discoverShows) {
        super.onNext(discoverShows);
        presenter.present(discoverShows);
    }

}
