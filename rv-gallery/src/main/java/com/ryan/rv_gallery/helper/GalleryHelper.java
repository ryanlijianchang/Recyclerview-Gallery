package com.ryan.rv_gallery.helper;

import com.ryan.rv_gallery.GalleryRecyclerView;

/**
 * Created by RyanLee on 2017/12/8.
 */

public class GalleryHelper {


    public static class Builder {
        private GalleryHelper mHelper;

        public Builder() {
            this.mHelper = new GalleryHelper();
        }

        public Builder attachRecyclerView(GalleryRecyclerView recyclerView) {
            RecyclerHelper recyclerHelper = new RecyclerHelper(recyclerView);
            recyclerHelper.initSnapHelper();
            recyclerHelper.initScrollListener();
            return this;
        }

        public Builder initPageParams(int pagePadding, int leftPageVisibleWidth) {
            GalleryAdapterHelper.newInstance().mPageMargin = pagePadding;
            GalleryAdapterHelper.newInstance().mLeftPageVisibleWidth = leftPageVisibleWidth;
            return this;
        }


    }

}
