package com.ataulm.wutson.show;

import java.net.URI;
import java.util.Iterator;
import java.util.List;

public class Show {

    private final String name;
    private final String overview;
    private final URI posterUri;
    private final Cast cast;

    Show(String name, String overview, URI posterUri, Cast cast) {
        this.name = name;
        this.overview = overview;
        this.posterUri = posterUri;
        this.cast = cast;
    }

    public String getName() {
        return name;
    }

    public URI getPosterUri() {
        return posterUri;
    }

    static class Cast implements Iterable<Character> {

        private final List<Character> characters;

        Cast(List<Character> characters) {
            this.characters = characters;
        }

        @Override
        public Iterator<Character> iterator() {
            return characters.iterator();
        }

    }

    static class Character {

        private final String name;
        private final Actor actor;

        Character(String name, Actor actor) {
            this.name = name;
            this.actor = actor;
        }

    }

    static class Actor {

        private final String name;

        Actor(String name) {
            this.name = name;
        }

    }

}
