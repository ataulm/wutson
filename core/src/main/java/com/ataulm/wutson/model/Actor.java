package com.ataulm.wutson.model;

import java.net.URI;

public class Actor {

    private final String name;
    private final URI profileUri;

    public Actor(String name, URI profileUri) {
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
