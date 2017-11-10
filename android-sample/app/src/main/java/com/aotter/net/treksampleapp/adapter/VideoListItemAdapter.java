package com.aotter.net.treksampleapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aotter.net.trek.ads.TKAdN;
import com.aotter.net.trek.ads.view.NativeVideoView;
import com.aotter.net.trek.model.NativeAd;
import com.aotter.net.treksampleapp.R;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by SmallMouth on 2016/12/13.
 */

public class VideoListItemAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context mContext;

    private TKAdN mTKAdN;
    private NativeAd mAd;
    private static final int AD_INDEX = 1;

    private View mAdView;

    private List<Object> mPostTitleList;
    private List<Object> mPostImageList;

    public VideoListItemAdapter(Context context, List<Object> titlelist, List<Object> imagelist) {
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
    public int getItemViewType(int position) {
        if (position == AD_INDEX && mAd != null)
            return 1;
        else
            return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);

        switch (type) {
            //add default item type to listview (Non ad)
            case 0:
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
                break;
            case 1:
                VideoViewHolder videoholder;
                if (convertView != null) {
                    videoholder = (VideoViewHolder) convertView.getTag();
                } else {
                    convertView = inflater.inflate(R.layout.advideo_item, parent, false);
                    videoholder = new VideoViewHolder(convertView);
                    convertView.setTag(videoholder);
                }

                // Setting the Text
                videoholder.nativeAdPublisher.setText(mAd.getAdSponsor());
                videoholder.nativeAdTitle.setText(mAd.getAdTitle());
                videoholder.nativeAdSummary.setText(mAd.getAdText());
                videoholder.nativeAdButton.setText(mAd.getActionText());

                mTKAdN.registerVideoViewForInteraction((Activity) mContext, videoholder.mAd_content_layout, videoholder.nativeVideoView, mAd);
                break;
        }
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

    class VideoViewHolder {

        @BindView(R.id.ad_content_layout)
        LinearLayout mAd_content_layout;

        @BindView(R.id.ad_publisher)
        TextView nativeAdPublisher;

        @BindView(R.id.ad_title)
        TextView nativeAdTitle;

        @BindView(R.id.ad_summary)
        TextView nativeAdSummary;

        @BindView(R.id.ad_button)
        Button nativeAdButton;

        @BindView(R.id.ad_videoview)
        NativeVideoView nativeVideoView;

        public VideoViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public synchronized void addNativeAd(TKAdN tkAdN, NativeAd ad) {
        if (tkAdN == null || ad == null) {
            return;
        }
        if (this.mAd != null) {
            this.mPostTitleList.remove(AD_INDEX);
            this.mPostImageList.remove(AD_INDEX);
            this.mAd = null;
            this.notifyDataSetChanged();
        }
        if (this.mTKAdN != null) {
            this.mTKAdN = null;
            this.notifyDataSetChanged();
        }
        this.mAd = ad;
        this.mTKAdN = tkAdN;
        mAdView = inflater.inflate(R.layout.advideo_item, null);
        mPostTitleList.add(AD_INDEX, mAdView);
        mPostImageList.add(AD_INDEX, mAdView);
        this.notifyDataSetChanged();
    }

}
