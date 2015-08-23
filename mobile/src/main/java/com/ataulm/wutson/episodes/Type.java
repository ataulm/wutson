package com.ataulm.wutson.episodes;

import com.ataulm.wutson.DeveloperError;

public enum Type {

    HEADER_SPACE,
    NAME,
    EPISODE_NUMBER,
    OVERVIEW;

    public static Type from(int ordinal) {
        for (Type type : values()) {
            if (type.ordinal() == ordinal) {
                return type;
            }
        }
        throw DeveloperError.because("Unknown type (ordinal: " + ordinal + ")");
    }

}
