package com.ryan.rv_gallery;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;

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
            // 理论消耗距离 = 卡片宽度 + 2倍边距
            // 卡片宽度 = 屏幕宽度 - 2倍pageVisibleWidth - 4倍pageMargin
            // 所以 理论消耗距离 = 屏幕宽度 - 2倍pageVisibleWidth - 2倍pageMargin
            int shouldConsumeX = OsUtil.getScreenWidth() - (pageVisibleWidth * 2 + pageMargin * 2);
            // 获取当前的位置
            int position = getPosition(mConsumeX, shouldConsumeX);

            float offset = (float) mConsumeX / (float) shouldConsumeX;     // 位置浮点值（即总消耗距离 / 每一页理论消耗距离 = 一个浮点型的位置值）

            // 避免offset值取整时进一，从而影响了percent值
            if (offset >= ((LinearLayoutManager) mGalleryRecyclerView.getLayoutManager()).findFirstVisibleItemPosition() + 1 && slideDirct == SLIDE_RIGHT) {
                return;
            }

            // 获取当前页移动的百分值
            float percent = offset - ((int) offset);

            // 设置动画变化
            AnimHelper.getInstance().setAnimation(recyclerView, position, percent);
        }
    }


    /**
     * 获取位置
     *
     * @param mConsumeX      实际消耗距离
     * @param shouldConsumeX 理论消耗距离
     * @return
     */
    private int getPosition(int mConsumeX, int shouldConsumeX) {
        float offset = (float) mConsumeX / (float) shouldConsumeX;
        int position = Math.round(offset);        // 四舍五入获取位置
        return position;
    }


}
