package com.admob.nativeads

import android.net.Uri
import android.util.Log
import android.view.View
import com.admob.nativeads.TrekAdapter.Companion.AD_TYPE_SUPR
import com.aotter.net.trek.ads.TKAdN
import com.aotter.net.trek.ads.view.TKMediaView
import com.aotter.net.trek.model.TKAdNative
import com.google.android.gms.ads.formats.NativeAd
import com.google.android.gms.ads.mediation.UnifiedNativeAdMapper
import java.util.*

class TrekUnifiedNativeAdMapper(private var tkAdN: TKAdN, private var tkAdNative: TKAdNative) : UnifiedNativeAdMapper() {

    protected val TAG = TrekUnifiedNativeAdMapper::class.java.simpleName

    init {
        headline = tkAdNative.adTitle
        body = tkAdNative.adText
        callToAction = tkAdNative.actionText
        icon = TrekNativeMappedImage(Uri.parse(tkAdNative.adImgIconHd), 1.0)
        images = listOf(TrekNativeMappedImage(Uri.parse(tkAdNative.adImgMain), 1.77))
        advertiser = tkAdNative.adSponsor

        if (tkAdNative.adType == AD_TYPE_SUPR){
            //TODO add Supr ad support
        }

        overrideClickHandling = false
        overrideImpressionRecording = false
    }

    override fun recordImpression() {
        tkAdN.recordTrekImp(tkAdNative)
    }

    override fun handleClick(View: View) {
        tkAdN.clickRegNativeAd(tkAdNative)
    }
}