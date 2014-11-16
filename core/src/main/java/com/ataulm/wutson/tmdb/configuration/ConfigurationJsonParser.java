package com.ataulm.wutson.tmdb.configuration;

import com.ataulm.wutson.JsonParser;
import com.google.gson.Gson;

public class ConfigurationJsonParser implements JsonParser<Configuration> {

    private final Gson gson;

    public static ConfigurationJsonParser newInstance() {
        return new ConfigurationJsonParser(new Gson());
    }

    ConfigurationJsonParser(Gson gson) {
        this.gson = gson;
    }

    @Override
    public Configuration parse(String json) {
        GsonConfiguration gsonConfiguration = gson.fromJson(json, GsonConfiguration.class);
        return Configuration.from(gsonConfiguration);
    }

}
