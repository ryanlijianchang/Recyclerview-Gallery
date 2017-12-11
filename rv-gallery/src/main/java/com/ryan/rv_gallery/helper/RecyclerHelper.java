package com.ryan.rv_gallery.helper;

import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;

import com.ryan.rv_gallery.GalleryRecyclerView;

/**
 * Created by RyanLee on 2017/12/8.
 */

public class RecyclerHelper {
    private GalleryRecyclerView mGalleryRecyclerView;

    private LinearSnapHelper mLinearySnapHelper;

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
        }
    }


}
