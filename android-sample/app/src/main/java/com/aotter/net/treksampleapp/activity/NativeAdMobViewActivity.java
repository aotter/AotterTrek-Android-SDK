package com.aotter.net.treksampleapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.admob.nativeads.TrekAdapter;
import com.aotter.net.treksampleapp.MainActivity;
import com.aotter.net.treksampleapp.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MediaContent;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import java.util.HashMap;

import static com.admob.nativeads.TrekAdapter.AD_CATEGORY;

public class NativeAdMobViewActivity extends AppCompatActivity {

    private AdLoader customEventNativeLoader;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_ad_mob_view);
        context = this;

        /**
         * Sample Custom Event Native ad.
         */
        customEventNativeLoader = new AdLoader.Builder(this,
                getResources().getString(R.string.customevent_native_ad_unit_id))
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        FrameLayout frameLayout =
                                (FrameLayout) findViewById(R.id.customeventnative_framelayout);
                        UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater()
                                .inflate(R.layout.ad_unified, null);
                        populateUnifiedNativeAdView(unifiedNativeAd, adView);
                        frameLayout.removeAllViews();
                        frameLayout.addView(adView);
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        Toast.makeText(NativeAdMobViewActivity.this,
                                "Custom event native ad failed with code: " + errorCode,
                                Toast.LENGTH_SHORT).show();
                    }
                }).build();


        final Bundle bundle = new Bundle();
        bundle.putSerializable(AD_CATEGORY, "your_category");

        customEventNativeLoader.loadAd(new AdRequest.Builder().addCustomEventExtrasBundle(TrekAdapter.class, bundle).build());
        Button refreshCustomEvent = (Button) findViewById(R.id.customeventnative_button);
        refreshCustomEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View unusedView) {
                customEventNativeLoader.loadAd(new AdRequest.Builder().addCustomEventExtrasBundle(TrekAdapter.class, bundle).build());
            }
        });

    }

    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        // Set the media view. Media content will be automatically populated in the media view once
        // adView.setNativeAd() is called.

        adView.setImageView(adView.findViewById(R.id.ad_img));

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline is guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());

            if (!isDestroy((Activity)context)&&!TextUtils.isEmpty(nativeAd.getIcon().getUri().toString()))
                Glide.with(context)
                        .load(nativeAd.getIcon().getUri())
                        .crossFade()
                        .into((ImageView) adView.getIconView());
            adView.getIconView().setVisibility(View.VISIBLE);
        }


        if (nativeAd.getImages() == null) {
            adView.getImageView().setVisibility(View.GONE);
        } else {
            if (!isDestroy((Activity)context)&&!TextUtils.isEmpty(nativeAd.getImages().get(0).getUri().toString()))
                Glide.with(context)
                        .load(nativeAd.getImages().get(0).getUri())
                        .crossFade()
                        .into((ImageView) adView.getImageView());
            adView.getImageView().setVisibility(View.VISIBLE);
        }


        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad. The SDK will populate the adView's MediaView
        // with the media content from this native ad.
        adView.setNativeAd(nativeAd);
    }


    public static boolean isDestroy(Activity activity) {
        if (activity == null || activity.isFinishing() || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed())) {
            return true;
        } else {
            return false;
        }
    }
}