package com.ryan.rv_gallery.util;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by RyanLee on 2017/11/29.
 */

public class OsUtil {
    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static float dipToPixels(float dipValue) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static float spToPt(int sp) {
        return sp * Resources.getSystem().getDisplayMetrics().density;
    }
}
