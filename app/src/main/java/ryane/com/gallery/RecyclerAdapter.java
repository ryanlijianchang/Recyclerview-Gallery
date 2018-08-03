package ryane.com.gallery;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ryan.rv_gallery.util.DLog;

import java.util.List;

/**
 *
 * @author RyanLee
 * @date 2017/12/7
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyHolder> {
    private Context mContext;
    private List<Integer> mDatas;


    RecyclerAdapter(Context mContext, List<Integer> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        DLog.d(MainActivity.TAG, "RecyclerAdapter onAttachedToRecyclerView");
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DLog.d(MainActivity.TAG, "RecyclerAdapter onCreateViewHolder" + " width = " + parent.getWidth());
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_gallery, parent, false);
        return new MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        DLog.d(MainActivity.TAG, "RecyclerAdapter onBindViewHolder" + "--> position = " + position);
        holder.mView.setImageResource(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        final ImageView mView;

        MyHolder(View itemView) {
            super(itemView);
            mView = itemView.findViewById(R.id.iv_photo);
        }
    }

    /**
     * 获取position位置的resId
     * @param position int
     * @return int
     */
    public int getResId(int position) {
        return mDatas == null ? 0 : mDatas.get(position);
    }
}
