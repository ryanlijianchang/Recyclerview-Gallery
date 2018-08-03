package ryane.com.gallery;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.ryan.rv_gallery.AnimManager;
import com.ryan.rv_gallery.GalleryRecyclerView;
import com.ryan.rv_gallery.util.DLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author RyanLee
 */
public class GalleryFragment extends Fragment implements GalleryRecyclerView.OnItemClickListener{
    public static final String TAG = "GalleryFragment";

    private GalleryRecyclerView mRecyclerView;
    private RelativeLayout mContainer;

    private Map<String, Drawable> mTSDraCacheMap = new HashMap<>();
    private static final String KEY_PRE_DRAW = "key_pre_draw";

    /**
     * 获取虚化背景的位置
     */
    private int mLastDraPosition = -1;

    public GalleryFragment() {
    }

    public static GalleryFragment newInstance() {
        GalleryFragment fragment = new GalleryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DLog.setDebug(false);

        DLog.d(TAG, "onCreate()");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mRecyclerView = view.findViewById(R.id.rv_list);
        mContainer = view.findViewById(R.id.rl_container);


        final RecyclerAdapter adapter = new RecyclerAdapter(getContext(), getDatas());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.initFlingSpeed(9000)                                   // 设置滑动速度（像素/s）
                .initPageParams(0, 60)     // 设置页边距和左右图片的可见宽度，单位dp
                .setAnimFactor(0.15f)                                   // 设置切换动画的参数因子
                .setAnimType(AnimManager.ANIM_BOTTOM_TO_TOP)            // 设置切换动画类型，目前有AnimManager.ANIM_BOTTOM_TO_TOP和目前有AnimManager.ANIM_TOP_TO_BOTTOM
                .setOnItemClickListener(this)                          // 设置点击事件
                .setUp();


        // 背景高斯模糊 & 淡入淡出
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    setBlurImage();
                }
            }
        });
        setBlurImage();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /***
     * 测试数据
     * @return List<Integer>
     */
    public List<Integer> getDatas() {
        TypedArray ar = getResources().obtainTypedArray(R.array.test_arr);
        final int[] resIds = new int[ar.length()];
        for (int i = 0; i < ar.length(); i++) {
            resIds[i] = ar.getResourceId(i, 0);
        }
        ar.recycle();
        List<Integer> tDatas = new ArrayList<>();
        for (int resId : resIds) {
            tDatas.add(resId);
        }
        return tDatas;
    }

    /**
     * 设置背景高斯模糊
     */
    public void setBlurImage() {
        RecyclerAdapter adapter = (RecyclerAdapter) mRecyclerView.getAdapter();
        final int mCurViewPosition = mRecyclerView.getScrolledPosition();

        if (adapter == null || mRecyclerView == null) {
            return;
        }
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                if (!isAdded()) {
                    // fix fragment not attached to Activity
                    return;
                }
                // 获取当前位置的图片资源ID
                int resourceId = ((RecyclerAdapter) mRecyclerView.getAdapter()).getResId(mCurViewPosition);
                // 将该资源图片转为Bitmap
                Bitmap resBmp = BitmapFactory.decodeResource(getResources(), resourceId);
                // 将该Bitmap高斯模糊后返回到resBlurBmp
                Bitmap resBlurBmp = BlurBitmapUtil.blurBitmap(mRecyclerView.getContext(), resBmp, 15f);
                // 再将resBlurBmp转为Drawable
                Drawable resBlurDrawable = new BitmapDrawable(resBlurBmp);
                // 获取前一页的Drawable
                Drawable preBlurDrawable = mTSDraCacheMap.get(KEY_PRE_DRAW) == null ? resBlurDrawable : mTSDraCacheMap.get(KEY_PRE_DRAW);

                /* 以下为淡入淡出效果 */
                Drawable[] drawableArr = {preBlurDrawable, resBlurDrawable};
                TransitionDrawable transitionDrawable = new TransitionDrawable(drawableArr);
                mContainer.setBackgroundDrawable(transitionDrawable);
                transitionDrawable.startTransition(500);

                // 存入到cache中
                mTSDraCacheMap.put(KEY_PRE_DRAW, resBlurDrawable);
                // 记录上一次高斯模糊的位置
                mLastDraPosition = mCurViewPosition;
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getContext(), "position=" + position, Toast.LENGTH_SHORT).show();
    }
}
