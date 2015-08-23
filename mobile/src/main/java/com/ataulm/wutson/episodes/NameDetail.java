package com.ataulm.wutson.episodes;

class NameDetail implements Detail {

    private final String name;

    NameDetail(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public Type getType() {
        return Type.NAME;
    }

}
