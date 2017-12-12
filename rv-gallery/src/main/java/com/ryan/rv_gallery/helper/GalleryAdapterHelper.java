package com.ryan.rv_gallery.helper;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.ryan.rv_gallery.util.OsUtil;

/***
 * Created by RyanLee on 2017/12/8.
 */
public class GalleryAdapterHelper {

    int mPageMargin = 0;          // 每一个页面默认页边距
    int mLeftPageVisibleWidth = 50; // 中间页面左右两边的页面可见部分宽度

    public static GalleryAdapterHelper mHelper;

    public static GalleryAdapterHelper newInstance() {
        if (mHelper == null) {
            mHelper = new GalleryAdapterHelper();
        }
        return mHelper;
    }

    public void setItemLayoutParams(final ViewGroup parent, final View itemView, final int position, final int itemCount) {

        parent.post(new Runnable() {
            @Override
            public void run() {

                int itemNewWidth = parent.getWidth() - OsUtil.dpToPx(4 * mPageMargin + 2 * mLeftPageVisibleWidth);
                int leftMargin = position == 0 ? OsUtil.dpToPx(mLeftPageVisibleWidth + 2 * mPageMargin) : OsUtil.dpToPx(mPageMargin);
                int rightMargin = position == itemCount - 1 ? OsUtil.dpToPx(mLeftPageVisibleWidth + 2 * mPageMargin) : OsUtil.dpToPx(mPageMargin);

                setLayoutParams(itemView, leftMargin, 0, rightMargin, 0, itemNewWidth);
            }
        });
    }

    private void setLayoutParams(View itemView, int left, int top, int right, int bottom, int itemWidth) {
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) itemView.getLayoutParams();
        boolean mMarginChange = false;
        boolean mWidthChange = false;

        if (lp.leftMargin != left || lp.topMargin != top || lp.rightMargin != right || lp.bottomMargin != bottom) {
            lp.setMargins(left, top, right, bottom);
            mMarginChange = true;
        }
        if (lp.width != itemWidth) {
            lp.width = itemWidth;
            mWidthChange = true;
        }

        if (mWidthChange || mMarginChange) {
            itemView.setLayoutParams(lp);
        }
    }
}
