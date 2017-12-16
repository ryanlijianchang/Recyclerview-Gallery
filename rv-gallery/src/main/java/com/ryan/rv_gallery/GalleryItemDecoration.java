package com.ryan.rv_gallery;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.ryan.rv_gallery.util.OsUtil;

/**
 * Created by RyanLee on 2017/12/14.
 */

public class GalleryItemDecoration extends RecyclerView.ItemDecoration {
    private final String TAG = "GalleryItemDecoration";

    public static int mPageMargin = 0;          // 每一个页面默认页边距
    public static int mLeftPageVisibleWidth = 50; // 中间页面左右两边的页面可见部分宽度

    public static int mItemComusemY = 0;
    public static int mItemComusemX = 0;

    private GalleryRecyclerView.OnItemClickListener onItemClickListener;

    public GalleryItemDecoration() {
    }

    @Override
    public void getItemOffsets(Rect outRect, final View view, final RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        Log.d(TAG, "getItemOffset() --> position = " + parent.getChildAdapterPosition(view));

        final int position = parent.getChildAdapterPosition(view);
        final int itemCount = parent.getAdapter().getItemCount();

        parent.post(new Runnable() {
            @Override
            public void run() {
                if (((GalleryRecyclerView) parent).getOrientation() == LinearLayoutManager.HORIZONTAL) {
                    onSetHoritiontalParams(parent, view, position, itemCount);
                } else {
                    onSetVerticalParams(parent, view, position, itemCount);
                }
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, position);
                }
            }
        });
    }

    private void onSetVerticalParams(ViewGroup parent, View itemView, int position, int itemCount) {
        int itemNewWidth = parent.getWidth();
        int itemNewHeight = parent.getHeight() - OsUtil.dpToPx(4 * mPageMargin + 2 * mLeftPageVisibleWidth);

        mItemComusemY = itemNewHeight + OsUtil.dpToPx(2 * mPageMargin);


        Log.d("TAG", "itemNewHeight=" + itemNewHeight);

        // 适配第0页和最后一页没有左页面和右页面，让他们保持左边距和右边距和其他项一样
        int topMargin = position == 0 ? OsUtil.dpToPx(mLeftPageVisibleWidth + 2 * mPageMargin) : OsUtil.dpToPx(mPageMargin);
        int bottomMargin = position == itemCount - 1 ? OsUtil.dpToPx(mLeftPageVisibleWidth + 2 * mPageMargin) : OsUtil.dpToPx(mPageMargin);

        setLayoutParams(itemView, 0, topMargin, 0, bottomMargin, itemNewWidth, itemNewHeight);
    }

    /**
     * 设置水平滚动的参数
     *
     * @param parent
     * @param itemView
     * @param position
     * @param itemCount
     */
    private void onSetHoritiontalParams(ViewGroup parent, View itemView, int position, int itemCount) {
        int itemNewWidth = parent.getWidth() - OsUtil.dpToPx(4 * mPageMargin + 2 * mLeftPageVisibleWidth);
        int itemNewHeight = parent.getHeight();

        mItemComusemX = itemNewWidth + OsUtil.dpToPx(2 * mPageMargin);

        Log.d(TAG, "onSetHoritiontalParams -->" + "parent.width=" + parent.getWidth() + ";mPageMargin=" + OsUtil.dpToPx(mPageMargin) + ";mLeftVis=" + OsUtil.dpToPx(mLeftPageVisibleWidth) + ";itemNewWidth=" + itemNewWidth);

        // 适配第0页和最后一页没有左页面和右页面，让他们保持左边距和右边距和其他项一样
        int leftMargin = position == 0 ? OsUtil.dpToPx(mLeftPageVisibleWidth + 2 * mPageMargin) : OsUtil.dpToPx(mPageMargin);
        int rightMargin = position == itemCount - 1 ? OsUtil.dpToPx(mLeftPageVisibleWidth + 2 * mPageMargin) : OsUtil.dpToPx(mPageMargin);

        setLayoutParams(itemView, leftMargin, 0, rightMargin, 0, itemNewWidth, itemNewHeight);
    }

    /**
     * 设置参数
     *
     * @param itemView
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @param itemWidth
     * @param itemHeight
     */
    private void setLayoutParams(View itemView, int left, int top, int right, int bottom, int itemWidth, int itemHeight) {
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) itemView.getLayoutParams();
        boolean mMarginChange = false;
        boolean mWidthChange = false;
        boolean mHeightChange = false;

        if (lp.leftMargin != left || lp.topMargin != top || lp.rightMargin != right || lp.bottomMargin != bottom) {
            lp.setMargins(left, top, right, bottom);
            mMarginChange = true;
        }
        if (lp.width != itemWidth) {
            lp.width = itemWidth;
            mWidthChange = true;
        }
        if (lp.height != itemHeight) {
            lp.height = itemHeight;
            mHeightChange = true;

        }

        // 因为方法会不断调用，只有在真正变化了之后才调用
        if (mWidthChange || mMarginChange || mHeightChange) {
            itemView.setLayoutParams(lp);
        }
    }

    public void setOnItemClickListener(GalleryRecyclerView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
