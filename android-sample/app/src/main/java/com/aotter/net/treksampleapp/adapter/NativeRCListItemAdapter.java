package com.aotter.net.treksampleapp.adapter;

import android.app.Activity;
import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aotter.net.trek.ads.TKAdN;
import com.aotter.net.trek.model.NativeAd;
import com.aotter.net.treksampleapp.R;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SmallMouth on 2018/2/6.
 */

public class NativeRCListItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private TKAdN mTKAdN;
    private NativeAd mAd;
    private static final int AD_INDEX = 5;
    private List<Object> mPostTitleList;
    private List<Object> mPostImageList;
    private Context mContext;

    private static final int TAG_ITEM = 0;
    private static final int TAG_AD = 1;

    public NativeRCListItemAdapter(Context context, List<Object> titlelist, List<Object> imagelist) {
        this.mContext = context;
        this.mPostTitleList = titlelist;
        this.mPostImageList = imagelist;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.post_publisher)
        TextView mPost_publisher;

        @BindView(R.id.post_image)
        ImageView mPost_image;

        @BindView(R.id.post_title)
        TextView mPost_title;

        @BindView(R.id.card_container)
        CardView mCard_container;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindView(int position) {
            mPost_title.setText((String) mPostTitleList.get(position));

            Glide.with(mContext)
                    .load((String) mPostImageList.get(position))
                    .crossFade()
                    .into(mPost_image);
        }
    }

    class AdViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ad_content_layout)
        LinearLayout mAd_content_layout;

        @BindView(R.id.ad_publisher)
        TextView nativeAdPublisher;

        @BindView(R.id.ad_image)
        ImageView nativeAdImage;

        @BindView(R.id.ad_title)
        TextView nativeAdTitle;

        @BindView(R.id.ad_summary)
        TextView nativeAdSummary;

        @BindView(R.id.ad_button)
        Button nativeAdButton;

        public AdViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindViewAdView(int position) {
            // Setting the Text

            Glide.with(mContext)
                    .load(mAd.getAdImg_main())
                    .crossFade()
                    .into(nativeAdImage);
            nativeAdPublisher.setText(mAd.getAdSponsor());
            nativeAdTitle.setText(mAd.getAdTitle());
            nativeAdSummary.setText(mAd.getAdText());
            nativeAdButton.setText(mAd.getActionText());

            mTKAdN.registerViewForInteraction((Activity) mContext, mAd_content_layout, mAd);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mTKAdN != null && mAd != null && position == AD_INDEX) {
            return TAG_AD;
        } else {
            return TAG_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        switch (viewType) {
            case TAG_AD:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.ad_item, parent, false);
                return new AdViewHolder(v);
            case TAG_ITEM:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.adapter_listpost, parent, false);
                return new ViewHolder(v);
            default:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.adapter_listpost, parent, false);
                return new ViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AdViewHolder) {
            AdViewHolder vh = (AdViewHolder) holder;
            vh.bindViewAdView(position);
        } else if (holder instanceof ViewHolder) {
            ViewHolder vh = (ViewHolder) holder;
            vh.bindView(position);
        }
    }

    @Override
    public int getItemCount() {
        return mPostTitleList.size();
    }

    public synchronized void addNativeAd(TKAdN tkAdN, NativeAd ad) {
        if (tkAdN == null || ad == null) {
            return;
        }
        if (this.mAd != null) {
            this.mAd = null;
            this.notifyDataSetChanged();
        }
        if (this.mTKAdN != null) {
            this.mTKAdN = null;
            this.notifyDataSetChanged();
        }
        this.mAd = ad;
        this.mTKAdN = tkAdN;
        mPostTitleList.add(AD_INDEX, mAd);
        mPostImageList.add(AD_INDEX, mAd);
        this.notifyDataSetChanged();
    }
}
