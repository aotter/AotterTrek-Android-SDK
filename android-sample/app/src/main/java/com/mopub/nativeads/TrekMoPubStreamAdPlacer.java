package com.mopub.nativeads;

import android.app.Activity;

import androidx.annotation.NonNull;


public class TrekMoPubStreamAdPlacer extends MoPubStreamAdPlacer {

    public TrekMoPubStreamAdPlacer(@NonNull Activity activity, @NonNull MoPubNativeAdPositioning.MoPubServerPositioning positioningSource, @NonNull TrekNativeAdSource adSource) {
        super(activity, adSource, new ServerPositioningSource(activity));
    }
}