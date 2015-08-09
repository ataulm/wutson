package com.ataulm.wutson.showdetails.view;

class OverviewDetail implements Detail {

    private final String overview;

    OverviewDetail(String overview) {
        this.overview = overview;
    }

    public String getOverview() {
        return overview;
    }

    @Override
    public Type getType() {
        return Type.OVERVIEW;
    }

}
