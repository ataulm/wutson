package com.ataulm.wutson;

import android.content.Context;
import android.widget.Toast;

public class ToastDisplayer {

    private final Context context;

    private Toast toast;

    public ToastDisplayer(Context context) {
        this.context = context.getApplicationContext();
    }

    public void display(String message) {
        displayDuration(message, Toast.LENGTH_SHORT);
    }

    public void displayLong(String message) {
        displayDuration(message, Toast.LENGTH_LONG);
    }

    private void displayDuration(String message, int duration) {
        cancelToast();
        toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    private void cancelToast() {
        if (toast != null) {
            toast.cancel();
        }
    }

}
