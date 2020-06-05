package com.mopub.nativeads;

import android.app.Activity;
import android.widget.Adapter;

import androidx.annotation.NonNull;



public class TrekMoPubAdAdapter extends MoPubAdAdapter {

    public TrekMoPubAdAdapter(@NonNull Activity activity, @NonNull Adapter originalAdapter, @NonNull MoPubNativeAdPositioning.MoPubServerPositioning adPositioning, TrekNativeAdSource adSource) {
        super(new TrekMoPubStreamAdPlacer(activity, adPositioning, adSource), originalAdapter,
                new VisibilityTracker(activity));
    }
}