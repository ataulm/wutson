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
    public DiscoverTv parse() {
        // TODO: clearly this input should be passed in parse(String input) when we do for realsies
        String input = MockDiscoverTv.JSON;

        GsonDiscoverTv gsonDiscoverTv = gson.fromJson(input, GsonDiscoverTv.class);
        return DiscoverTv.from(gsonDiscoverTv);
    }

}
