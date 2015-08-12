package com.ataulm.wutson;

public interface DataSet<T> {

    int getItemCount();

    T getItem(int position);

    long getItemId(int position);

}
