package com.ryan.rv_gallery.helper;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by RyanLee on 2017/12/12.
 */

public class AnimHelper {
    private static final float mAnimFactor = 0.2f;


    /**
     * 从下到上的动画效果
     *
     * @param recyclerView
     * @param position
     * @param percent
     */
    public static void setBottomToTopAnim(RecyclerView recyclerView, int position, float percent) {
        View mCurView = recyclerView.getLayoutManager().findViewByPosition(position);       // 中间页
        View mRightView = recyclerView.getLayoutManager().findViewByPosition(position + 1); // 左边页
        View mLeftView = recyclerView.getLayoutManager().findViewByPosition(position - 1);  // 右边页

        if (percent <= 0.5) {
            if (mLeftView != null) {
                mLeftView.setScaleX((1 - mAnimFactor) + percent * mAnimFactor);
                mLeftView.setScaleY((1 - mAnimFactor) + percent * mAnimFactor);
            }
            if (mCurView != null) {
                mCurView.setScaleX(1 - percent * mAnimFactor);
                mCurView.setScaleY(1 - percent * mAnimFactor);
            }
            if (mRightView != null) {
                mRightView.setScaleX((1 - mAnimFactor) + percent * mAnimFactor);
                mRightView.setScaleY((1 - mAnimFactor) + percent * mAnimFactor);
            }
        } else {
            if (mLeftView != null) {
                mLeftView.setScaleX(1 - percent * mAnimFactor);
                mLeftView.setScaleY(1 - percent * mAnimFactor);
            }
            if (mCurView != null) {
                mCurView.setScaleX((1 - mAnimFactor) + percent * mAnimFactor);
                mCurView.setScaleY((1 - mAnimFactor) + percent * mAnimFactor);
            }
            if (mRightView != null) {
                mRightView.setScaleX(1 - percent * mAnimFactor);
                mRightView.setScaleY(1 - percent * mAnimFactor);
            }
        }
    }


    /***
     * 从上到下的效果
     * @param recyclerView
     * @param position
     * @param percent
     */
    public static void setTopToBottomAnim(RecyclerView recyclerView, int position, float percent) {
        View mCurView = recyclerView.getLayoutManager().findViewByPosition(position);       // 中间页
        View mRightView = recyclerView.getLayoutManager().findViewByPosition(position + 1); // 左边页
        View mLeftView = recyclerView.getLayoutManager().findViewByPosition(position - 1);  // 右边页

        if (percent <= 0.5) {
            if (mLeftView != null) {
                mLeftView.setScaleX(1 - percent * mAnimFactor);
                mLeftView.setScaleY(1 - percent * mAnimFactor);
            }
            if (mCurView != null) {
                mCurView.setScaleX((1 - mAnimFactor) + percent * mAnimFactor);
                mCurView.setScaleY((1 - mAnimFactor) + percent * mAnimFactor);
            }
            if (mRightView != null) {
                mRightView.setScaleX(1 - percent * mAnimFactor);
                mRightView.setScaleY(1 - percent * mAnimFactor);
            }

        } else {
            if (mLeftView != null) {
                mLeftView.setScaleX((1 - mAnimFactor) + percent * mAnimFactor);
                mLeftView.setScaleY((1 - mAnimFactor) + percent * mAnimFactor);
            }
            if (mCurView != null) {
                mCurView.setScaleX(1 - percent * mAnimFactor);
                mCurView.setScaleY(1 - percent * mAnimFactor);
            }
            if (mRightView != null) {
                mRightView.setScaleX((1 - mAnimFactor) + percent * mAnimFactor);
                mRightView.setScaleY((1 - mAnimFactor) + percent * mAnimFactor);
            }
        }
    }
}
