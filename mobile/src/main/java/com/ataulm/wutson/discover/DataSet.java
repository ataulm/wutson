package com.ataulm.wutson.discover;

interface DataSet<T> {

    int getItemCount();

    T getItem(int position);

    long getItemId(int position);

}
