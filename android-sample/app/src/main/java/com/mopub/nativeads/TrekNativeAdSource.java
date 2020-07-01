package com.mopub.nativeads;

import java.util.HashMap;
import java.util.Map;

public class TrekNativeAdSource extends NativeAdSource {

    private Map<String, Object> mLocalExtras = new HashMap<>();

    @Override
    void loadAds(RequestParameters requestParameters, MoPubNative moPubNative) {
        moPubNative.setLocalExtras(mLocalExtras);
        super.loadAds(requestParameters, moPubNative);
    }

    public void setLocalExtras(Map<String, Object> localExtras) {
        this.mLocalExtras = localExtras;
    }
}