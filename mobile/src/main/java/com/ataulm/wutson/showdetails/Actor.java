package com.ataulm.wutson.showdetails;

import java.net.URI;

class Actor {

    private final String name;
    private final URI profileUri;

    Actor(String name, URI profileUri) {
        this.name = name;
        this.profileUri = profileUri;
    }

    String getName() {
        return name;
    }

    URI getProfileUri() {
        return profileUri;
    }

}
