package ryane.com.gallery;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.ryan.rv_gallery.helper.GalleryHelper;
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

        new GalleryHelper.Builder()
                .attachRecyclerView(mRecyclerView);
    }


}
