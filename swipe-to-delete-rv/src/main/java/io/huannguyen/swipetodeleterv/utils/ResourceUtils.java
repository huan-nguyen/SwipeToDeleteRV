package io.huannguyen.swipetodeleterv.utils;

import android.content.Context;
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
}
