package com.ataulm.wutson.view;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.annotation.ColorRes;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.ataulm.wutson.R;

public class CrossfadeRoundedLeftImageView extends ImageView {

    private static final int CROSSFADE_DURATION_MS = 350;

    public CrossfadeRoundedLeftImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setImageBitmap(Bitmap bitmap) {
        int cardCornerRadiusPixel = getResources().getDimensionPixelSize(R.dimen.card_corner_radius);
        MatrixBitmapDrawable matrixBitmapDrawable = new LeftRoundedBitmapDrawable(R.color.transparent, new ColorDrawable(Color.TRANSPARENT), cardCornerRadiusPixel);
        matrixBitmapDrawable.setBitmap(bitmap);

        TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{
                new ColorDrawable(Color.TRANSPARENT),
                matrixBitmapDrawable
        });

        super.setImageDrawable(transitionDrawable);
        transitionDrawable.startTransition(CROSSFADE_DURATION_MS);
    }

    private static class LeftRoundedBitmapDrawable extends RoundRectTopCenterBitmapDrawable {

        public LeftRoundedBitmapDrawable(@ColorRes int placeholderBgColor, Drawable placeholder, float cornerRadius) {
            super(placeholderBgColor, placeholder, cornerRadius);
        }

        @Override
        protected RectF updateCachedBounds(Rect bounds) {
            return new RectF(
                    bounds.left,
                    bounds.top,
                    bounds.right + 2 * getCornerRadius(),
                    bounds.bottom
            );
        }
    }

    private static class RoundRectTopCenterBitmapDrawable extends RoundRectBitmapDrawable {

        public RoundRectTopCenterBitmapDrawable(@ColorRes int placeholderBgColor, Drawable placeholder, float cornerRadius) {
            super(placeholderBgColor, placeholder, cornerRadius);
        }

        /**
         * This implementation calculates a Matrix that fills the viewport scaling the bitmap
         * and aligning ALWAYS the content to the top.
         * <p/>
         * If the image is wider than the viewport it crops the bitmap on the sides maintaining the
         * image top aligned.
         * <p/>
         * If the image is taller than the viewport it scales the bitmap to match the viewport width
         * and crops all the content at the bottom outside the viewport.
         *
         * @param bitmapWidth
         * @param bitmapHeight
         * @param bounds
         * @return
         */
        @Override
        protected Matrix calculateMatrix(int bitmapWidth, int bitmapHeight, Rect bounds) {
            Matrix matrix = new Matrix();
            int width = bounds.width();
            int height = bounds.height();
            float dx = 0;

            float scale;
            // check if the bitmap is wider than the viewport
            if (bitmapWidth * height > width * bitmapHeight) {
                // scale the bitmap in order to make
                // its height to match the viewport one
                scale = (float) height / (float) bitmapHeight;

                // calculate the translation on X needed to crop
                // the content that doesn't fit in
                dx = (width - bitmapWidth * scale) * 0.5f;
            } else {
                // scale the bitmap in order to make
                // its width to match the viewport one
                scale = (float) width / (float) bitmapWidth;

                // NOTE: no translation over Y here
            }
            matrix.setScale(scale, scale);
            matrix.postTranslate((int) (dx + 0.5f), 0);
            return matrix;
        }

    }

    private static class RoundRectBitmapDrawable extends MatrixBitmapDrawable {

        private final float cornerRadius;

        public RoundRectBitmapDrawable(@ColorRes int placeholderBgColor, Drawable placeholder, float cornerRadius) {
            super(placeholderBgColor, placeholder);
            this.cornerRadius = cornerRadius;
        }

        protected void innerDraw(Canvas canvas, RectF bounds, Paint bitmapPaint) {
            canvas.drawRoundRect(bounds, cornerRadius, cornerRadius, bitmapPaint);
        }

        public float getCornerRadius() {
            return cornerRadius;
        }

    }

    private static class MatrixBitmapDrawable extends Drawable {

        private final Drawable placeholderImage;
        private final Paint bitmapPaint;

        private Paint placeholderBgPaint;
        private Bitmap bitmap;
        private BitmapShader bitmapShader;
        private int bitmapWidth;
        private int bitmapHeight;
        private RectF cachedBounds;
        private Rect placeholderBounds;

        public MatrixBitmapDrawable(@ColorRes int placeholderBgColor, Drawable placeholder) {
            this.bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG);
            this.placeholderBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            this.placeholderBgPaint.setColor(placeholderBgColor);
            this.placeholderImage = placeholder;
            this.placeholderBounds = new Rect();
            this.cachedBounds = new RectF();
        }

        protected int getBitmapWidth() {
            return bitmapWidth;
        }

        protected int getBitmapHeight() {
            return bitmapHeight;
        }

        @Override
        protected void onBoundsChange(Rect bounds) {
            if (bounds.width() == 0 || bounds.height() == 0) {
                return;
            }

            cachedBounds = updateCachedBounds(bounds);
            placeholderBounds.set(bounds);

            if (bitmapWidth == 0 || bitmapHeight == 0) {
                return;
            }

            Matrix matrix = calculateMatrix(bitmapWidth, bitmapHeight, bounds);
            bitmapShader.setLocalMatrix(matrix);
        }

        /**
         * The default implementation calculates a Matrix suitable for applying the well known
         * ScaleType.CENTER_CROP from ImageView. It fills the viewport scaling the bitmap
         * and perhaps cropping the content outside the viewport itself.
         */
        protected Matrix calculateMatrix(int bitmapWidth, int bitmapHeight, Rect bounds) {
            Matrix matrix = new Matrix();
            float scale;
            float dx = 0, dy = 0;
            int width = bounds.width();
            int height = bounds.height();

            // Check if the bitmap is wider than the viewport
            if (bitmapWidth * height > width * bitmapHeight) {
                // Scale the bitmap in order to make
                // its height to match the viewport one
                scale = (float) height / (float) bitmapHeight;

                // Calculate the translation on X needed to crop
                // the content that doesn't fit in
                dx = (width - bitmapWidth * scale) * 0.5f;
            } else {
                // Scale the bitmap in order to make
                // its width to match the viewport one
                scale = (float) width / (float) bitmapWidth;

                // Calculate the translation on Y needed to crop
                // the content that doesn't fit in
                dy = (height - bitmapHeight * scale) * 0.5f;
            }

            matrix.setScale(scale, scale);
            matrix.postTranslate((int) (dx + 0.5f), (int) (dy + 0.5f));
            return matrix;
        }

        protected RectF updateCachedBounds(Rect bounds) {
            return new RectF(bounds);
        }

        @Override
        public void draw(Canvas canvas) {
            if (bitmap != null) {
                innerDraw(canvas, cachedBounds, bitmapPaint);
            } else {
                innerDraw(canvas, cachedBounds, placeholderBgPaint);
                drawPlaceholderImage(canvas);
            }
        }

        private void drawPlaceholderImage(Canvas canvas) {
            final int width = placeholderImage.getIntrinsicWidth();
            final int height = placeholderImage.getIntrinsicHeight();
            final int left = placeholderBounds.left + (placeholderBounds.width() / 2 - width / 2);
            final int top = placeholderBounds.top + (placeholderBounds.height() / 2 - height / 2);
            placeholderImage.setBounds(left, top, left + width, top + height);

            placeholderImage.draw(canvas);
        }

        protected void innerDraw(Canvas canvas, RectF bounds, Paint bitmapPaint) {
            canvas.drawRect(bounds, bitmapPaint);
        }

        @Override
        public void setAlpha(int alpha) {
            bitmapPaint.setAlpha(alpha);
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
            bitmapPaint.setColorFilter(cf);
        }

        @Override
        public int getOpacity() {
            return 0;
        }

        public void setBitmap(Bitmap bitmap) {
            if (bitmap != null) {
                bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                bitmapWidth = bitmap.getWidth();
                bitmapHeight = bitmap.getHeight();
                bitmapPaint.setShader(bitmapShader);
            } else {
                bitmapShader = null;
                bitmapPaint.setShader(null);
                bitmapWidth = bitmapHeight = 0;
            }
            this.bitmap = bitmap;
            onBoundsChange(getBounds());
            invalidateSelf();
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

    }

}
