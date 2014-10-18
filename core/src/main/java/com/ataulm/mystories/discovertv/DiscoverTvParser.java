package com.ataulm.mystories.discovertv;

import com.ataulm.mystories.Parser;
import com.google.gson.Gson;

public class DiscoverTvParser implements Parser<DiscoverTv> {

    private final Gson gson;

    public static DiscoverTvParser newInstance() {
        return new DiscoverTvParser(new Gson());
    }

    DiscoverTvParser(Gson gson) {
        this.gson = gson;
    }

    @Override
    public DiscoverTv parse(String json) {
        GsonDiscoverTv gsonDiscoverTv = gson.fromJson(json, GsonDiscoverTv.class);
        return DiscoverTv.from(gsonDiscoverTv);
    }

}
