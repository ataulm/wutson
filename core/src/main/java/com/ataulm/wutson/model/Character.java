package com.ataulm.wutson.model;

public class Character {

    private final String name;
    private final Actor actor;

    public Character(String name, Actor actor) {
        this.name = name;
        this.actor = actor;
    }

    public String getName() {
        return name;
    }

    public Actor getActor() {
        return actor;
    }

}
