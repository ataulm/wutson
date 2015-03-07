package com.ataulm.wutson.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.CheckedTextView;

import com.ataulm.wutson.R;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

public class WutsonTextView extends CheckedTextView {

    private final TypefaceFactory typefaceFactory;

    public WutsonTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.typefaceFactory = new TypefaceFactory();

        applyCustomAttributes(context, attrs);
    }

    private void applyCustomAttributes(Context context, AttributeSet attrs) {
        if (isInEditMode()) {
            return;
        }

        Typeface typeface = typefaceFactory.createFrom(context, attrs);
        int style = TypefaceFactory.extractStyle(context, attrs);
        setTypeface(typeface, style);
    }

    private static class TypefaceFactory {

        /**
         * order must match with attrs.xml > styleable=WutsonTextView
         */
        public enum FontType {

            ROBOTO_MEDIUM("fonts/Roboto-Medium.ttf"),
            ROBOTO_REGULAR("fonts/Roboto-Regular.ttf");

            private final String assetUrl;

            FontType(String assetUrl) {
                this.assetUrl = assetUrl;
            }

        }

        private static final Map<FontType, SoftReference<Typeface>> FONT_CACHE = new HashMap<>();
        private static final int INVALID_FONT_ID = -1;
        private static final Typeface DEFAULT_TYPEFACE = null;

        public static int extractStyle(Context context, AttributeSet attrs) {
            int[] attrValues = {android.R.attr.textStyle};
            TypedArray typedArray = context.obtainStyledAttributes(attrs, attrValues);
            if (typedArray == null) {
                return Typeface.NORMAL;
            }
            try {
                return typedArray.getInt(0, Typeface.NORMAL);
            } finally {
                typedArray.recycle();
            }
        }

        public Typeface createFrom(Context context, AttributeSet attrs) {
            int fontId = getFontId(context, attrs);
            if (!isValidId(fontId)) {
                return DEFAULT_TYPEFACE;
            }
            FontType fontType = getFontType(fontId);
            return getTypeFace(context, fontType);
        }

        private int getFontId(Context context, AttributeSet attrs) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WutsonTextView);
            if (typedArray == null) {
                return INVALID_FONT_ID;
            }

            try {
                return typedArray.getInt(R.styleable.WutsonTextView_textFont, INVALID_FONT_ID);
            } finally {
                typedArray.recycle();
            }
        }

        private boolean isValidId(int fontId) {
            return fontId > INVALID_FONT_ID && fontId < FontType.values().length;
        }

        private FontType getFontType(int fontId) {
            return FontType.values()[fontId];
        }

        private Typeface getTypeFace(Context context, FontType fontType) {
            synchronized (FONT_CACHE) {
                if (fontExistsInCache(fontType)) {
                    return getCachedTypeFace(fontType);
                }

                Typeface typeface = createTypeFace(context, fontType);
                saveFontToCache(fontType, typeface);

                return typeface;
            }
        }

        private boolean fontExistsInCache(FontType fontType) {
            return FONT_CACHE.get(fontType) != null && getCachedTypeFace(fontType) != null;
        }

        private Typeface getCachedTypeFace(FontType fontType) {
            return FONT_CACHE.get(fontType).get();
        }

        private Typeface createTypeFace(Context context, FontType fontType) {
            return Typeface.createFromAsset(context.getAssets(), fontType.assetUrl);
        }

        private void saveFontToCache(FontType fontType, Typeface typeface) {
            FONT_CACHE.put(fontType, new SoftReference<Typeface>(typeface));
        }

    }

}
