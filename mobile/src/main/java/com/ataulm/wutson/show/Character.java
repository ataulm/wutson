package com.ataulm.wutson.show;

class Character {

    private final String name;
    private final Actor actor;

    Character(String name, Actor actor) {
        this.name = name;
        this.actor = actor;
    }

    String getName() {
        return name;
    }

    Actor getActor() {
        return actor;
    }

}