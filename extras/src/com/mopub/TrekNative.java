package com.mopub.nativeads;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.aotter.net.trek.ads.TKAdN;
import com.aotter.net.trek.ads.interfaces.AdListener;
import com.aotter.net.trek.ads.view.NativeVideoView;
import com.aotter.net.trek.ads.view.TrekMediaView;
import com.aotter.net.trek.model.NativeAd;
import com.mopub.common.Preconditions;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mopub.nativeads.NativeImageHelper.preCacheImages;

/**
 * Created by SmallMouth on 2016/11/17.
 */

public class TrekNative extends CustomEventNative {
    private static final String TAG = "TrekAdRendererDebug";

    private static final String PLACE_NAME_KEY = "place_name";
    private static final String TREK_ADTYPE_KEY = "adType";

    private static final String TREK_ADTYPE_NATIVE = "NATIVE";
    private static final String TREK_ADTYPE_NATIVE_VIDEO = "NATIVE_VIDEO";
    private static final String TREK_ADTYPE_NATIVE_INTERACTIVE = "NATIVE_INTERACTIVE";

    private static Boolean sIsVideoRendererAvailable = null;

    @Override
    protected void loadNativeAd(@NonNull final Context context,
                                @NonNull final CustomEventNativeListener customEventNativeListener,
                                @NonNull final Map<String, Object> localExtras,
                                @NonNull final Map<String, String> serverExtras) {

        final String placeName;
        if (extrasAreValid(serverExtras)) {
            placeName = serverExtras.get(PLACE_NAME_KEY);
        } else {
            customEventNativeListener.onNativeAdFailed(NativeErrorCode.NATIVE_ADAPTER_CONFIGURATION_ERROR);
            return;
        }

        final String trekAdType;

        if (serverExtras.containsKey(TREK_ADTYPE_KEY)) {
            trekAdType = serverExtras.get(TREK_ADTYPE_KEY);
        } else {
            trekAdType = "NATIVE";
        }

        boolean videoEnabledFromServer = TextUtils.equals(trekAdType, TREK_ADTYPE_NATIVE_VIDEO) || TextUtils.equals(trekAdType, TREK_ADTYPE_NATIVE_INTERACTIVE);

        if (sIsVideoRendererAvailable == null) {
            try {
                Class.forName("com.mopub.nativeads.TrekAdRenderer");
                sIsVideoRendererAvailable = true;
            } catch (ClassNotFoundException e) {
                sIsVideoRendererAvailable = false;
            }
        }

        if (shouldUseVideoEnabledNativeAd(sIsVideoRendererAvailable, videoEnabledFromServer)) {
            final TrekMediaEnabledNativeAd trekMediaEnabledNativeAd =
                    new TrekMediaEnabledNativeAd(context,
                            new TKAdN(context, placeName, trekAdType), customEventNativeListener);
            trekMediaEnabledNativeAd.loadAd();
        } else {
            final TrekStaticNativeAd trekStaticNativeAd = new TrekStaticNativeAd(
                    context, new TKAdN(context, placeName, trekAdType),
                    new ImpressionTracker(context),
                    new NativeClickHandler(context),
                    customEventNativeListener);
            trekStaticNativeAd.loadAd();
        }
    }

    private boolean extrasAreValid(final Map<String, String> serverExtras) {
        final String placeName = serverExtras.get(PLACE_NAME_KEY);
        return (placeName != null && placeName.length() > 0);
    }

    static boolean shouldUseVideoEnabledNativeAd(final boolean isVideoRendererAvailable, final boolean videoEnabledFromServer) {
        if (!isVideoRendererAvailable) {
            return false;
        }
        return videoEnabledFromServer;
    }


    static class TrekStaticNativeAd extends StaticNativeAd implements AdListener {
        private static final String SPONSOR_CONTEXT_FOR_AD = "sponsor";
        private static final String ADVERTISER_NAME_FOR_AD = "advertiserName";
        private static final String TREK_DEV_NATIVE_AD = "devNativeAd";

        private final Context mContext;
        private final TKAdN mTKAdN;
        private final ImpressionTracker mImpressionTracker;
        private final NativeClickHandler mNativeClickHandler;
        private final CustomEventNativeListener mCustomEventNativeListener;

        private String mImpUrl;
        private String mAdTrekClickUrl;
        private String mAdClickUrl;

        TrekStaticNativeAd(final Context context,
                           final TKAdN tkAdN,
                           final ImpressionTracker impressionTracker,
                           final NativeClickHandler nativeClickHandler,
                           final CustomEventNativeListener customEventNativeListener) {
            mContext = context.getApplicationContext();
            mTKAdN = tkAdN;
            mImpressionTracker = impressionTracker;
            mNativeClickHandler = nativeClickHandler;
            mCustomEventNativeListener = customEventNativeListener;
        }

        void loadAd() {
            mTKAdN.setAdListener(this);
        }

        @Override
        public void onAdLoaded(NativeAd nativeAd) {
            NativeAd mNativeAd = nativeAd;
            if (mNativeAd == null) {
                mCustomEventNativeListener.onNativeAdFailed(NativeErrorCode.NETWORK_INVALID_STATE);
                return;
            }

            setTitle(mNativeAd.getAdTitle());
            setText(mNativeAd.getAdText());

            setMainImageUrl(mNativeAd.getAdImg_main());
            setIconImageUrl(mNativeAd.getAdImg_icon_hd());
            mImpUrl = mTKAdN.urlImpression(mNativeAd);
            mAdClickUrl = mTKAdN.urlClick(mNativeAd);
            mAdTrekClickUrl = mTKAdN.urlTrek(mNativeAd);

            if (!mNativeAd.isOutAppBrowser()) {
                setClickDestinationUrl(mTKAdN.urlClick(mNativeAd));
            }

            addExtra(SPONSOR_CONTEXT_FOR_AD, mNativeAd.getAdSponsor());
            addExtra(ADVERTISER_NAME_FOR_AD, mNativeAd.getAdAdvertiserName());
            addExtra(TREK_DEV_NATIVE_AD, mNativeAd);

            final List<String> imageUrls = new ArrayList<String>();
            final String mainImageUrl = getMainImageUrl();
            if (mainImageUrl != null) {
                imageUrls.add(getMainImageUrl());
            }
            final String iconUrl = getIconImageUrl();
            if (iconUrl != null) {
                imageUrls.add(getIconImageUrl());
            }

            preCacheImages(mContext, imageUrls, new NativeImageHelper.ImageListener() {
                @Override
                public void onImagesCached() {
                    mCustomEventNativeListener.onNativeAdLoaded(TrekStaticNativeAd.this);
                }

                @Override
                public void onImagesFailedToCache(NativeErrorCode errorCode) {
                    mCustomEventNativeListener.onNativeAdFailed(errorCode);
                }
            });
        }

        @Override
        public void onAdFail() {
            mCustomEventNativeListener.onNativeAdFailed(NativeErrorCode.UNSPECIFIED);
        }

        @Override
        public void onAdClicked(NativeAd nativeAd) {

        }

        @Override
        public void onAdImpression(NativeAd nativeAd) {

        }


        // Lifecycle Handlers
        @Override
        public void prepare(final View view) {
            // Must access these methods directly to get impressions to fire.
            mImpressionTracker.addView(view, this);
            mNativeClickHandler.setOnClickListener(view, this);
        }

        @Override
        public void clear(final View view) {
            mImpressionTracker.removeView(view);
            mNativeClickHandler.clearOnClickListener(view);
        }

        @Override
        public void destroy() {
            mImpressionTracker.destroy();
        }


        // Event Handlers
        @Override
        public void recordImpression(final View view) {
            notifyAdImpressed();
            NativeAd ad = (NativeAd) getExtra(TREK_DEV_NATIVE_AD);
            mTKAdN.recordTrekImp(ad);
        }

        @Override
        public void handleClick(final View view) {
            notifyAdClicked();
            NativeAd ad = (NativeAd) getExtra(TREK_DEV_NATIVE_AD);
            if (!TextUtils.isEmpty(getClickDestinationUrl())) {
                mNativeClickHandler.openClickDestinationUrl(getClickDestinationUrl(), view);
                mTKAdN.recordTrekClick(ad);
            } else {
                mTKAdN.clickRegNativeAd(mAdClickUrl, mAdTrekClickUrl);
            }
        }

    }


    static class TrekMediaEnabledNativeAd extends BaseNativeAd implements AdListener {
        private static final String SPONSOR_CONTEXT_FOR_AD = "sponsor";
        private static final String TREK_ADTYPE = "trekAdType";
        private static final String ADVERTISER_NAME_FOR_AD = "advertiserName";

        static final double MIN_STAR_RATING = 0;
        static final double MAX_STAR_RATING = 5;

        private final Context mContext;
        private final TKAdN mTKAdN;
        private final CustomEventNativeListener mCustomEventNativeListener;

        private final Map<String, Object> mExtras;
        private NativeAd mNativeAd;

        TrekMediaEnabledNativeAd(final Context context,
                                 final TKAdN tkAdN,
                                 final CustomEventNativeListener customEventNativeListener) {
            mContext = context.getApplicationContext();
            mTKAdN = tkAdN;
            mCustomEventNativeListener = customEventNativeListener;
            mExtras = new HashMap<String, Object>();
        }

        void loadAd() {
            mTKAdN.setAdListener(this);
        }

        /**
         * Returns the String corresponding to the ad's title.
         */
        final public String getTitle() {
            return mNativeAd.getAdTitle();
        }

        /**
         * Returns the String corresponding to the ad's body text. May be null.
         */
        final public String getText() {
            return mNativeAd.getAdText();
        }

        /**
         * Returns the String url corresponding to the ad's main image. May be null.
         */
        final public String getMainImageUrl() {
            return mNativeAd.getAdImg_main();
        }

        /**
         * Returns the String url corresponding to the ad's icon image. May be null.
         */
        final public String getIconImageUrl() {
            return mNativeAd.getAdImg_icon_hd();
        }

        /**
         * Returns the Call To Action String (i.e. "Download" or "Learn More") associated with this ad.
         */
        final public String getCallToAction() {
            return mNativeAd.getActionText();
        }

        @Override
        public void onAdLoaded(NativeAd nativeAd) {
            mNativeAd = nativeAd;
            if (mNativeAd == null) {
                mCustomEventNativeListener.onNativeAdFailed(NativeErrorCode.NETWORK_INVALID_STATE);
                return;
            }

            addExtra(SPONSOR_CONTEXT_FOR_AD, mNativeAd.getAdSponsor());
            addExtra(ADVERTISER_NAME_FOR_AD, mNativeAd.getAdAdvertiserName());
            addExtra(TREK_ADTYPE, mNativeAd.getAdType());

            final List<String> imageUrls = new ArrayList<String>();
            final String mainImageUrl = getMainImageUrl();
            if (mainImageUrl != null) {
                imageUrls.add(mainImageUrl);
            }
            final String iconImageUrl = getIconImageUrl();
            if (iconImageUrl != null) {
                imageUrls.add(iconImageUrl);
            }

            preCacheImages(mContext, imageUrls, new NativeImageHelper.ImageListener() {
                @Override
                public void onImagesCached() {
                    mCustomEventNativeListener.onNativeAdLoaded(TrekMediaEnabledNativeAd.this);
                }

                @Override
                public void onImagesFailedToCache(NativeErrorCode errorCode) {
                    mCustomEventNativeListener.onNativeAdFailed(errorCode);
                }
            });

        }

        @Override
        public void onAdFail() {
            mCustomEventNativeListener.onNativeAdFailed(NativeErrorCode.UNSPECIFIED);
        }

        @Override
        public void onAdClicked(NativeAd nativeAd) {

        }

        @Override
        public void onAdImpression(NativeAd nativeAd) {

        }

        /**
         * Returns a copy of the extras map, reflecting additional ad content not reflected in any
         * of the above hardcoded setters. This is particularly useful for passing down custom fields
         * with MoPub's direct-sold native ads or from mediated networks that pass back additional
         * fields.
         */
        final public Map<String, Object> getExtras() {
            return new HashMap<String, Object>(mExtras);
        }


        final public void addExtra(final String key, final Object value) {
            if (!Preconditions.NoThrow.checkNotNull(key, "addExtra key is not allowed to be null")) {
                return;
            }
            mExtras.put(key, value);
        }

        @Override
        public void prepare(@NonNull View view) {
        }

        @Override
        public void clear(@NonNull View view) {

        }

        @Override
        public void destroy() {

        }

        public void updateVideoView(Activity mActivity, View view, NativeVideoView nativeVideoView) {
            if (nativeVideoView != null) {
                mTKAdN.registerVideoViewForInteraction(mActivity, view, nativeVideoView, mNativeAd);
            }
        }

        public void updateMediaView(Activity mActivity, TrekMediaView trekMediaView) {
            if (trekMediaView != null) {
                mTKAdN.registerViewForInteraction(mActivity, trekMediaView, mNativeAd);
            }
        }
    }


}
