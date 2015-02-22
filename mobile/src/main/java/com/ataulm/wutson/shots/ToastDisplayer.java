package com.ataulm.wutson.shots;

import android.content.Context;
import android.widget.Toast;

public class ToastDisplayer {

    private final Context context;

    private Toast toast;

    public ToastDisplayer(Context context) {
        this.context = context.getApplicationContext();
    }

    public void display(String message) {
        cancelToast();
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void cancelToast() {
        if (toast != null) {
            toast.cancel();
        }
    }

}
