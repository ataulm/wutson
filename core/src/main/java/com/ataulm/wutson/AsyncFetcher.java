package com.ataulm.wutson;

import rx.Observable;

public interface AsyncFetcher<T> {

    Observable<T> fetch();

}
