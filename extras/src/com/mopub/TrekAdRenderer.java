package com.mopub.nativeads;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aotter.net.trek.ads.view.NativeVideoView;
import com.aotter.net.trek.ads.view.TrekMediaView;
import com.mopub.common.Preconditions;

import java.util.WeakHashMap;

import static android.view.View.VISIBLE;

/**
 * Created by SmallMouth on 2017/8/8.
 */

public class TrekAdRenderer implements MoPubAdRenderer<TrekNative.TrekMediaEnabledNativeAd> {
    private final ViewBinder mViewBinder;
    private static final String TAG = "TrekAdRendererDebug";
    private static final String TREK_ADTYPE = "trekAdType";

    final WeakHashMap<View, TrekNativeViewHolder> mViewHolderMap;
    private final Activity mActivity;
    private final boolean mInterActiveShowAction;
    private View mView;
    private boolean isMediaBackgroundBlack = false;
    /**
     * Constructs a native ad renderer with a view binder.
     *
     * @param interActiveShowAction
     * @param viewBinder            The view binder to use when inflating and rendering an ad.
     */
    public TrekAdRenderer(final Activity activity, boolean interActiveShowAction, final ViewBinder viewBinder) {
        mViewBinder = viewBinder;
        mViewHolderMap = new WeakHashMap<View, TrekNativeViewHolder>();
        mActivity = activity;
        mInterActiveShowAction = interActiveShowAction;
    }

    @NonNull
    @Override
    public View createAdView(@NonNull Context context, @Nullable ViewGroup parent) {
        final View adView = LayoutInflater
                .from(context)
                .inflate(mViewBinder.layoutId, parent, false);

        return adView;
    }

    @Override
    public void renderAdView(@NonNull View view,
                             @NonNull TrekNative.TrekMediaEnabledNativeAd trekMediaEnabledNativeAd) {
        this.mView = view;
        TrekNativeViewHolder trekNativeViewHolder = mViewHolderMap.get(view);
        if (trekNativeViewHolder == null) {
            trekNativeViewHolder = TrekNativeViewHolder.fromViewBinder(view, mViewBinder);
            mViewHolderMap.put(view, trekNativeViewHolder);
        }

        update(view, trekNativeViewHolder, trekMediaEnabledNativeAd);
        NativeRendererHelper.updateExtras(trekNativeViewHolder.getMainView(),
                mViewBinder.extras,
                trekMediaEnabledNativeAd.getExtras());
        setViewVisibility(trekNativeViewHolder, VISIBLE);
    }

    @Override
    public boolean supports(@NonNull BaseNativeAd nativeAd) {
        Preconditions.checkNotNull(nativeAd);
        return nativeAd instanceof TrekNative.TrekMediaEnabledNativeAd;
    }


    private void update(final View view,
                        final TrekNativeViewHolder trekNativeViewHolder,
                        final TrekNative.TrekMediaEnabledNativeAd nativeAd) {

        final ImageView mainImageView = trekNativeViewHolder.getMainImageView();
        NativeRendererHelper.addTextView(trekNativeViewHolder.getTitleView(),
                nativeAd.getTitle());
        NativeRendererHelper.addTextView(trekNativeViewHolder.getTextView(), nativeAd.getText());
        NativeRendererHelper.addTextView(trekNativeViewHolder.getCallToActionView(),
                nativeAd.getCallToAction());
        NativeImageHelper.loadImageView(nativeAd.getMainImageUrl(), mainImageView);
        NativeImageHelper.loadImageView(nativeAd.getIconImageUrl(),
                trekNativeViewHolder.getIconImageView());

        final NativeVideoView nativeVideoView = trekNativeViewHolder.getNativeVideoView();
        final TrekMediaView trekMediaView = trekNativeViewHolder.getTrekMediaView();

        final String adtype = nativeAd.getExtras().get(TREK_ADTYPE).toString();

        if (TextUtils.equals(adtype, "NATIVE_INTERACTIVE")) {
            nativeAd.updateMediaView(mActivity, trekMediaView);
            trekMediaView.setVisibility(View.VISIBLE);
            trekMediaView.setMediaBackgroundBlack(isMediaBackgroundBlack);
            if (mInterActiveShowAction) {
                trekNativeViewHolder.getCallToActionView().setVisibility(VISIBLE);
            } else {
                trekNativeViewHolder.getCallToActionView().setVisibility(View.GONE);
            }
        } else if (nativeVideoView != null) {
            nativeAd.updateVideoView(mActivity, view, nativeVideoView);
            nativeVideoView.setVisibility(View.VISIBLE);
        }
    }

    private static void setViewVisibility(final TrekNativeViewHolder trekNativeViewHolder,
                                          final int visibility) {
        if (trekNativeViewHolder.getMainView() != null) {
            trekNativeViewHolder.getMainView().setVisibility(visibility);
        }
    }

    public TrekMediaView getMediaView() {
        if (mView != null && mViewBinder.extras.containsKey("mediaView")) {
            return (TrekMediaView) mView.findViewById(mViewBinder.extras.get("mediaView"));
        }
        return null;
    }

    public void setMediaBackgroundBlack(boolean mediaBackgroundBlack) {
        isMediaBackgroundBlack = mediaBackgroundBlack;
    }

    static class TrekNativeViewHolder {
        private final StaticNativeViewHolder mStaticNativeViewHolder;
        private final NativeVideoView mNativeVideoView;
        private final TrekMediaView mTrekMediaView;

        // Use fromViewBinder instead of a constructor
        private TrekNativeViewHolder(final StaticNativeViewHolder staticNativeViewHolder,
                                     final NativeVideoView nativeVideoView,
                                     final TrekMediaView trekMediaView) {
            mStaticNativeViewHolder = staticNativeViewHolder;
            mNativeVideoView = nativeVideoView;
            mTrekMediaView = trekMediaView;
        }

        static TrekNativeViewHolder fromViewBinder(final View view,
                                                   final ViewBinder viewBinder) {
            StaticNativeViewHolder staticNativeViewHolder = StaticNativeViewHolder.fromViewBinder(view, viewBinder);
            NativeVideoView nativeVideoView = null;
            TrekMediaView trekMediaView = null;

            if (viewBinder.extras.containsKey("videoView")) {
                nativeVideoView = (NativeVideoView) view.findViewById(viewBinder.extras.get("videoView"));
            }

            if (viewBinder.extras.containsKey("mediaView")) {
                trekMediaView = (TrekMediaView) view.findViewById(viewBinder.extras.get("mediaView"));
            }

            return new TrekNativeViewHolder(staticNativeViewHolder, nativeVideoView, trekMediaView);
        }

        public View getMainView() {
            return mStaticNativeViewHolder.mainView;
        }

        public TextView getTitleView() {
            return mStaticNativeViewHolder.titleView;
        }

        public TextView getTextView() {
            return mStaticNativeViewHolder.textView;
        }

        public TextView getCallToActionView() {
            return mStaticNativeViewHolder.callToActionView;
        }

        public ImageView getMainImageView() {
            return mStaticNativeViewHolder.mainImageView;
        }

        public ImageView getIconImageView() {
            return mStaticNativeViewHolder.iconImageView;
        }

        public ImageView getPrivacyInformationIconImageView() {
            return mStaticNativeViewHolder.privacyInformationIconImageView;
        }

        public NativeVideoView getNativeVideoView() {
            return mNativeVideoView;
        }

        public TrekMediaView getTrekMediaView() {
            return mTrekMediaView;
        }
    }

}
