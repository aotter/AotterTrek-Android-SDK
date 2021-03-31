package com.aotter.net.treksampleapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.aotter.net.treksampleapp.R;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by SmallMouth on 2016/12/13.
 */

public class AdListItemAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private final Context mContext;

    private final List<Object> mPostTitleList;
    private final List<Object> mPostImageList;

    public AdListItemAdapter(Context context, List<Object> titlelist, List<Object> imagelist) {
        this.inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        this.mContext = context;
        this.mPostTitleList = titlelist;
        this.mPostImageList = imagelist;
    }

    @Override
    public int getCount() {
        return mPostTitleList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(R.layout.adapter_listpost, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        holder.mPost_title.setText((String) mPostTitleList.get(position));

        Glide.with(mContext)
                .load((String) mPostImageList.get(position))
                .crossFade()
                .into(holder.mPost_image);
        return convertView;
    }

    class ViewHolder {

        @BindView(R.id.post_publisher)
        TextView mPost_publisher;

        @BindView(R.id.post_image)
        ImageView mPost_image;

        @BindView(R.id.post_title)
        TextView mPost_title;

        @BindView(R.id.card_container)
        CardView mCard_container;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
