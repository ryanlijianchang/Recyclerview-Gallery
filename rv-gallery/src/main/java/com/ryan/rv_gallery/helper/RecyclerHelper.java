package com.ryan.rv_gallery.helper;

import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;

import com.ryan.rv_gallery.GalleryRecyclerView;
import com.ryan.rv_gallery.util.DLog;
import com.ryan.rv_gallery.util.OsUtil;

/**
 * Created by RyanLee on 2017/12/8.
 */

public class RecyclerHelper {

    private GalleryRecyclerView mGalleryRecyclerView;

    private LinearSnapHelper mLinearySnapHelper;

    // 使偏移量为左边距 + 左边Item的可视部分宽度
    private int mConsumeX = OsUtil.dpToPx(GalleryAdapterHelper.newInstance().mLeftPageVisibleWidth + GalleryAdapterHelper.newInstance().mPageMargin * 2);
    // 滑动方向
    private int slideDirct = SLIDE_RIGHT;

    private static final int SLIDE_LEFT = 1;    // 左滑
    private static final int SLIDE_RIGHT = 2;   // 右滑


    public RecyclerHelper(GalleryRecyclerView mGalleryRecyclerView) {
        this.mGalleryRecyclerView = mGalleryRecyclerView;
    }

    /**
     * 初始化SnapHelper
     */
    public void initSnapHelper() {
        mLinearySnapHelper = new LinearSnapHelper();
        mLinearySnapHelper.attachToRecyclerView(mGalleryRecyclerView);
    }

    /**
     * 监听RecyclerView的滑动
     */
    public void initScrollListener() {
        GalleryScrollerListener mScrollerListener = new GalleryScrollerListener();
        mGalleryRecyclerView.addOnScrollListener(mScrollerListener);
    }


    class GalleryScrollerListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            mConsumeX += dx;

            if (dx > 0) {
                // 右滑
                slideDirct = SLIDE_RIGHT;
            } else {
                // 左滑
                slideDirct = SLIDE_LEFT;
            }

            int pageVisibleWidth = OsUtil.dpToPx(GalleryAdapterHelper.newInstance().mLeftPageVisibleWidth);
            int pageMargin = OsUtil.dpToPx(GalleryAdapterHelper.newInstance().mPageMargin);
            // 获取卡片的宽度(屏幕宽度 - （2倍左卡片可见宽度 + 4倍页边距))
            int itemWidth = OsUtil.getScreenWidth() - (pageVisibleWidth * 2 + pageMargin * 4);
            // 获取当前的位置
            int position = getPosition(mConsumeX, itemWidth);
        }
    }

    /**
     * 获取位置
     *
     * @param mConsumeX
     * @param itemWidth
     * @return
     */
    private int getPosition(int mConsumeX, int itemWidth) {
        float position = mConsumeX / itemWidth;     // 位置浮点值
        int roundPos = Math.round(position);        // 四舍五入获取位置值
        DLog.d("getPosition() --> mConsumeX = " + mConsumeX + "; itemWidth = " + itemWidth + "; position = " + position + "; roundPos = " + roundPos);
        return roundPos;
    }


}
