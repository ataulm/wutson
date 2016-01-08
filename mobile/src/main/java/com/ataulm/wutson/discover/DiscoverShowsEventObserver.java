package com.ataulm.wutson.discover;

import com.ataulm.wutson.DeveloperError;
import com.ataulm.wutson.jabber.Jabber;
import com.ataulm.wutson.repository.event.Event;
import com.ataulm.wutson.repository.event.Optional;
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

        switch (event.getType()) {
            case LOADING:
                // TODO: show loading throbber
                break;
            case ERROR:
                // TODO: notify user if appropriate
                break;
            case IDLE:
                // TODO: hide loading throbber
                break;
            default:
                throw DeveloperError.onUnexpectedSwitchCase(event.getType());
        }
    }

    private void presentLatestDataFrom(Event<DiscoverShows> event) {
        Optional<DiscoverShows> data = event.getData();
        if (data.isPresent()) {
            DiscoverShows discoverShows = data.get();
            // TODO: guard against loading same data twice here? or in presenter?
            presenter.present(discoverShows);
        }
    }

}
