package ryane.com.gallery;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.ryan.rv_gallery.AnimManager;
import com.ryan.rv_gallery.GalleryRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GalleryRecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_list);

        RecyclerAdapter adapter = new RecyclerAdapter(getApplicationContext(), testDatas());

        LinearLayoutManager mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.initFlingSpeed(5000).initPageParams(0, 60).setAnimFactor(0.15f).setAnimType(AnimManager.ANIM_BOTTOM_TO_TOP);
    }


    /***
     * 测试数据
     * @return
     */
    public List<Integer> testDatas() {
        List<Integer> tDatas = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            tDatas.add(R.drawable.star1);
            tDatas.add(R.drawable.star2);
            tDatas.add(R.drawable.star3);
            tDatas.add(R.drawable.star4);
            tDatas.add(R.drawable.star5);
            tDatas.add(R.drawable.star6);
            tDatas.add(R.drawable.star1);
            tDatas.add(R.drawable.star8);
            tDatas.add(R.drawable.star9);
        }
        return tDatas;
    }


}
