package com.ataulm.wutson.show;

import java.net.URI;

public class Actor {

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
