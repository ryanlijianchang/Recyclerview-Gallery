package com.ryan.rv_gallery;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by RyanLee on 2017/12/8.
 */

public class GalleryRecyclerView extends RecyclerView {
    private int FLING_SPEED = 1000; // 滑动速度


    public GalleryRecyclerView(Context context) {
        this(context, null);
    }

    public GalleryRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GalleryRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        attachToRecyclerHelper();
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        velocityX = balancelocity(velocityX);
        velocityY = balancelocity(velocityY);
        return super.fling(velocityX, velocityY);
    }

    /**
     * 返回滑动速度值
     *
     * @param velocity
     * @return
     */
    private int balancelocity(int velocity) {
        if (velocity > 0) {
            return Math.min(velocity, FLING_SPEED);
        } else {
            return Math.max(velocity, -FLING_SPEED);
        }
    }

    /**
     * 连接RecyclerHelper
     */
    private void attachToRecyclerHelper() {
        RecyclerHelper recyclerHelper = new RecyclerHelper(this);
        recyclerHelper.initSnapHelper();
        recyclerHelper.initScrollListener();
    }

    /**
     * 设置页面参数，单位dp
     *
     * @param pageMargin           默认：0dp
     * @param leftPageVisibleWidth 默认：50dp
     * @return
     */
    public GalleryRecyclerView initPageParams(int pageMargin, int leftPageVisibleWidth) {
        GalleryAdapterHelper.newInstance().mPageMargin = pageMargin;
        GalleryAdapterHelper.newInstance().mLeftPageVisibleWidth = leftPageVisibleWidth;
        return this;
    }

    /**
     * 设置滑动速度（像素/s）
     *
     * @param speed
     * @return
     */
    public GalleryRecyclerView initFlingSpeed(int speed) {
        this.FLING_SPEED = speed;
        return this;
    }

    /**
     * 设置动画因子
     *
     * @param factor
     * @return
     */
    public GalleryRecyclerView setAnimFactor(float factor) {
        AnimHelper.getInstance().setmAnimFactor(factor);
        return this;
    }

    /**
     * 设置动画类型
     *
     * @param type
     * @return
     */
    public GalleryRecyclerView setAnimType(int type) {
        AnimHelper.getInstance().setmAnimType(type);
        return this;
    }

    public int getOrientation() {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();
        if (linearLayoutManager == null) {
            try {
                throw new Exception("请设置LayoutManager为LinearLayoutManager");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return linearLayoutManager.getOrientation();
        }
        return 0;
    }

    public LinearLayoutManager getLinearLayoutManager() {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();
        if (linearLayoutManager == null) {
            try {
                throw new Exception("请设置LayoutManager为LinearLayoutManager");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return linearLayoutManager;
        }
        return null;
    }


}
