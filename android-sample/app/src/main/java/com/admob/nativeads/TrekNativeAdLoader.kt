package com.admob.nativeads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.aotter.net.trek.ads.TKAdN
import com.aotter.net.trek.ads.TKError
import com.aotter.net.trek.ads.interfaces.TKAdListener
import com.aotter.net.trek.model.TKAdNative

class TrekNativeAdLoader(val tkAdN: TKAdN) {
    protected val TAG = TrekNativeAdLoader::class.java.simpleName

    var listener: TrekNativeAdListener? = null

    fun fetchAd() {
            tkAdN.setAdListener(object : TKAdListener {

                override fun onAdLoaded(nativeAd: TKAdNative) {
                    listener?.onNativeAdFetched(nativeAd)
                }

                override fun onAdError(tkError: TKError) {}
                override fun onAdClicked(nativeAd: TKAdNative) {}
                override fun onAdImpression(nativeAd: TKAdNative) {}
            })

    }

}