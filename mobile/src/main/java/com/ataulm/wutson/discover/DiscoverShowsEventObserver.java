package com.ataulm.wutson.discover;

import com.ataulm.wutson.DeveloperError;
import com.ataulm.wutson.jabber.Jabber;
import com.ataulm.wutson.repository.event.Event;
import com.ataulm.wutson.Optional;
import com.ataulm.wutson.rx.LoggingObserver;
import com.ataulm.wutson.shows.discover.DiscoverShows;

class DiscoverShowsEventObserver extends LoggingObserver<Event<DiscoverShows>> {

    private final Presenter presenter;

    DiscoverShowsEventObserver(Presenter presenter) {
        super(Jabber.log());
        this.presenter = presenter;
    }

    @Override
    public void onNext(Event<DiscoverShows> event) {
        super.onNext(event);
        presentLatestDataFrom(event);
        reactToTypeOf(event);
    }

    private void presentLatestDataFrom(Event<DiscoverShows> event) {
        Optional<DiscoverShows> data = event.getData();
        if (data.isPresent()) {
            DiscoverShows discoverShows = data.get();
            presenter.present(discoverShows);
        }
    }

    private void reactToTypeOf(Event<DiscoverShows> event) {
        switch (event.getType()) {
            case LOADING:
                // RETRO: atm this will obscure the content
                presenter.onLoadStart();
                break;
            case ERROR:
                presenter.onError(event.getError().get());
                break;
            case IDLE:
                presenter.onLoadStop();
                break;
            default:
                throw DeveloperError.onUnexpectedSwitchCase(event.getType());
        }
    }

}
