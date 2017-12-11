package com.ryan.rv_gallery.util;

import android.util.Log;

/**
 * Created by RyanLee on 2017/12/11.
 */

public class DLog {
    public static boolean Debug = false;

    private static final String TAG = "DLog";

    public static void v(String tag, String msg) {
        if (!Debug) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (!Debug) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (!Debug) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (!Debug) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (!Debug) {
            Log.e(tag, msg);
        }
    }

    public static void v(String msg) {
        if (!Debug) {
            Log.v(TAG, msg);
        }
    }

    public static void d(String msg) {
        if (!Debug) {
            Log.d(TAG, msg);
        }
    }

    public static void i(String msg) {
        if (!Debug) {
            Log.i(TAG, msg);
        }
    }

    public static void w(String msg) {
        if (!Debug) {
            Log.w(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (!Debug) {
            Log.e(TAG, msg);
        }
    }
}
