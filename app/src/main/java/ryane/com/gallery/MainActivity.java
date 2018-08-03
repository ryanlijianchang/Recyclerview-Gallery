package ryane.com.gallery;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * @author RyanLee
 */
public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity_TAG";

    private FloatingActionButton mGalleryBtn, mBlankBtn;

    private GalleryFragment mGalleryFragment;
    private BlankFragment mBlankFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGalleryBtn = findViewById(R.id.fab_fragment_gallery);
        mBlankBtn = findViewById(R.id.fab_fragment_blank);

        mGalleryFragment = GalleryFragment.newInstance();
        mBlankFragment = BlankFragment.newInstance();

        mGalleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transitionManager = getSupportFragmentManager().beginTransaction();
                transitionManager.replace(R.id.rl_container, mGalleryFragment);
                transitionManager.commit();
            }
        });

        mBlankBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transitionManager = getSupportFragmentManager().beginTransaction();
                transitionManager.replace(R.id.rl_container, mBlankFragment);
                transitionManager.commit();
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }
}
