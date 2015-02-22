package com.ataulm.wutson.shots;

import android.support.v7.graphics.Palette;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class Swatches {

    private final Map<URI, Palette.Swatch> cache = new HashMap<>();

    public boolean hasSwatchFor(URI imageUri) {
        return cache.containsKey(imageUri);
    }

    public void put(URI imageUri, Palette.Swatch swatch) {
        cache.put(imageUri, swatch);
    }

    public Palette.Swatch get(URI imageUri) {
        return cache.get(imageUri);
    }

}
