package com.ataulm.wutson.show;

import java.net.URI;

class Actor {

    private final String name;
    private final URI profileUri;

    Actor(String name, URI profileUri) {
        this.name = name;
        this.profileUri = profileUri;
    }

    public String getName() {
        return name;
    }

    public URI getProfileUri() {
        return profileUri;
    }

}
