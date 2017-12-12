package ryane.com.gallery;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.ryan.rv_gallery.AnimHelper;
import com.ryan.rv_gallery.GalleryRecyclerView;

public class MainActivity extends AppCompatActivity {

    private GalleryRecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_list);

        RecyclerAdapter adapter = new RecyclerAdapter(getApplicationContext(), TestUtil.testDatas());
        LinearLayoutManager mManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.initFlingSpeed(7000).initPageParams(0, 30).setAnimFactor(0.3f).setAnimType(AnimHelper.ANIM_TOP_TO_BOTTOM);
    }


}
