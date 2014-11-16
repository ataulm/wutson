package com.ataulm.wutson.tmdb.discovertv;

import com.ataulm.wutson.JsonParser;
import com.google.gson.Gson;

public class DiscoverTvJsonParser implements JsonParser<DiscoverTv> {

    private final Gson gson;

    public static DiscoverTvJsonParser newInstance() {
        return new DiscoverTvJsonParser(new Gson());
    }

    DiscoverTvJsonParser(Gson gson) {
        this.gson = gson;
    }

    @Override
    public DiscoverTv parse(String json) {
        GsonDiscoverTv gsonDiscoverTv = gson.fromJson(json, GsonDiscoverTv.class);
        return DiscoverTv.from(gsonDiscoverTv);
    }

}
