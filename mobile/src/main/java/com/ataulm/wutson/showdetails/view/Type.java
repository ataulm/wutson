package com.ataulm.wutson.showdetails.view;

import com.ataulm.wutson.DeveloperError;

public enum Type {

    HEADER_SPACE,
    OVERVIEW,
    CAST_TITLE,
    CHARACTER;

    public static Type from(int ordinal) {
        for (Type type : values()) {
            if (type.ordinal() == ordinal) {
                return type;
            }
        }
        throw DeveloperError.because("Unknown type (ordinal: " + ordinal + ")");
    }

}
