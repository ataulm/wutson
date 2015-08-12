package com.ataulm.wutson.search;

interface DataSet<T> {

    int getItemCount();

    T getItem(int position);

    long getItemId(int position);

}
