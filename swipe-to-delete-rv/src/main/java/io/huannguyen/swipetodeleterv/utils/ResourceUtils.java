package io.huannguyen.swipetodeleterv.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.v4.content.ContextCompat;

/**
 * Created by huannguyen on 1/06/2016.
 */

public class ResourceUtils {

    public static int getColor(Context context, @ColorRes int color) {
        return ContextCompat.getColor(context, color);
    }

    public static float getDimension(Context context, @DimenRes int dimen) {
        return context.getResources().getDimension(dimen);
    }

    /**
     * Change color of a bitmap
     *
     * @param sourceBitmap  The bitmap whose color is to be changed
     * @param color         The new color of the bitmap
     * @return              A bitmap with new color
     */
    public static Bitmap changeBitmapColor(Bitmap sourceBitmap, int color) {
        Bitmap resultBitmap = sourceBitmap.copy(Bitmap.Config.ARGB_8888, true);
        ColorFilter filter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN);
        Paint paint = new Paint();
        paint.setColorFilter(filter);

        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(resultBitmap, 0, 0, paint);
        sourceBitmap.recycle();
        return resultBitmap;
    }
}
