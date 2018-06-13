package com.ryan.rv_gallery.util;

import android.util.Log;

/**
 * Created by RyanLee on 2018/2/2.
 */

public class DLog {

    private static boolean isDebug = false;

    public static void v(String tag, String msg) {
        if (isDebug) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (isDebug) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug) {
            Log.e(tag, msg);
        }
    }


    public static void setDebug(boolean debug) {
        isDebug = debug;
    }
}
