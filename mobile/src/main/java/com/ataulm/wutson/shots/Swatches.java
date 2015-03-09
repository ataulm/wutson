package com.ataulm.wutson.shots;

import android.support.v7.graphics.Palette;

import java.util.HashMap;
import java.util.Map;

public class Swatches {

    private final Map<String, Palette.Swatch> cache = new HashMap<>();

    public boolean hasSwatchFor(String id) {
        return cache.containsKey(id);
    }

    public void put(String id, Palette.Swatch swatch) {
        cache.put(id, swatch);
    }

    public Palette.Swatch get(String id) {
        return cache.get(id);
    }

}
