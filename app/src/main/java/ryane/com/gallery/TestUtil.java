package ryane.com.gallery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RyanLee on 2017/12/8.
 */

public class TestUtil {
    public static List<Integer> testDatas() {
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
