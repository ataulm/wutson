package com.ataulm.wutson.showdetails.view;

class CastTitleDetail implements Detail {

    private final String title;

    CastTitleDetail(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public Type getType() {
        return Type.CAST_TITLE;
    }

}
