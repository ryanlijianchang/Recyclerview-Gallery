package com.ryan.rv_gallery;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by RyanLee on 2017/12/8.
 */

public class GalleryRecyclerView extends RecyclerView {
    private static final int FLING_SPEED = 1000; // 滑动速度


    public GalleryRecyclerView(Context context) {
        this(context, null);
    }

    public GalleryRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GalleryRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        velocityX = balancelocity(velocityX);
        velocityY = balancelocity(velocityY);
        return super.fling(velocityX, velocityY);
    }

    private int balancelocity(int velocity) {
        if (velocity > 0) {
            return Math.min(velocity, FLING_SPEED);
        } else {
            return Math.max(velocity, -FLING_SPEED);
        }
    }
}
