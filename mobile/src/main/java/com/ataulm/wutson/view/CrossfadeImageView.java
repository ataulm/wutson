package com.ataulm.wutson.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CrossfadeImageView extends ImageView {

    private static final int CROSSFADE_DURATION_MS = 350;

    public CrossfadeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setImageBitmap(Bitmap bitmap) {
        TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{
                new ColorDrawable(Color.TRANSPARENT),
                new BitmapDrawable(getResources(), bitmap)
        });

        super.setImageDrawable(transitionDrawable);
        transitionDrawable.startTransition(CROSSFADE_DURATION_MS);
    }

}
