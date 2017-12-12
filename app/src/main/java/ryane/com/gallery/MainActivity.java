package ryane.com.gallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.RelativeLayout;

import com.ryan.rv_gallery.AnimManager;
import com.ryan.rv_gallery.GalleryRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private GalleryRecyclerView mRecyclerView;
    private RelativeLayout mContainer;

    private Map<String, Drawable> mTSDraCacheMap = new HashMap<>();
    private static final String KEY_PRE_DRAW = "key_pre_draw";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_list);
        mContainer = findViewById(R.id.rl_container);

        final RecyclerAdapter adapter = new RecyclerAdapter(getApplicationContext(), testDatas());

        LinearLayoutManager mManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.initFlingSpeed(5000).initPageParams(0, 60).setAnimFactor(0.15f).setAnimType(AnimManager.ANIM_BOTTOM_TO_TOP);

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

    /**
     * 设置背景高斯模糊
     */
    public void setBlurImage() {
        RecyclerAdapter adapter = (RecyclerAdapter) mRecyclerView.getAdapter();

        if (adapter == null || mRecyclerView == null) {
            return;
        }
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                // 获取当前位置的图片资源ID
                int resourceId = ((RecyclerAdapter) mRecyclerView.getAdapter()).getResId(mRecyclerView.getScrolledPosition());
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
            }
        });
    }


    /***
     * 测试数据
     * @return
     */
    public List<Integer> testDatas() {
        List<Integer> tDatas = new ArrayList<>();
        tDatas.add(R.drawable.beauty1);
        tDatas.add(R.drawable.beauty2);
        tDatas.add(R.drawable.beauty3);
        tDatas.add(R.drawable.beauty4);
        tDatas.add(R.drawable.beauty5);
        tDatas.add(R.drawable.beauty6);
        tDatas.add(R.drawable.beauty7);
        tDatas.add(R.drawable.beauty8);
        tDatas.add(R.drawable.beauty9);
        tDatas.add(R.drawable.beauty10);
        tDatas.add(R.drawable.beauty11);
        tDatas.add(R.drawable.beauty12);
        tDatas.add(R.drawable.beauty13);
        return tDatas;
    }


}
