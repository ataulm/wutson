package com.ataulm.wutson.trakt;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class JsonShowPeople {

    @SerializedName("cast")
    public Cast cast;

    public static class Cast extends ArrayList<Character> {
    }

    public static class Character {

        @SerializedName("character")
        public String character;

        @SerializedName("person")
        public Person person;

    }

    public static class Person {

        @SerializedName("name")
        public String name;

        @SerializedName("biography")
        public String biography;

        @SerializedName("birthday")
        public String birthday; // formatted as "1969-06-11"

        @SerializedName("death")
        public String death; // formatted as "1969-06-11"

        @SerializedName("images")
        public Images images;

    }

    public static class Images {

        @SerializedName("headshot")
        public Headshot headshot;

    }

    public static class Headshot {

        @SerializedName("full")
        public String full;

        @SerializedName("medium")
        public String medium;

        @SerializedName("thumb")
        public String thumb;

    }

}
