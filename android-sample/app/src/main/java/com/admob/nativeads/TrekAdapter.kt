package com.admob.nativeads

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.aotter.net.trek.ads.TKAdN
import com.google.android.gms.ads.mediation.NativeMediationAdRequest
import com.google.android.gms.ads.mediation.customevent.CustomEventNative
import com.google.android.gms.ads.mediation.customevent.CustomEventNativeListener

open class TrekAdapter : CustomEventNative {
    protected val TAG = TrekAdapter::class.java.simpleName
    private lateinit var tkAdN: TKAdN

    companion object {
        const val AD_CATEGORY = "category"
        const val AD_TYPE_NATIVE = "NATIVE"
        const val AD_TYPE_SUPR = "SUPR_AD"

    }

    override fun onDestroy() {
        tkAdN.destroy()
    }

    override fun onPause() {
        tkAdN.pause()
    }

    override fun onResume() {
        tkAdN.resume()
    }

    override fun requestNativeAd(context: Context?,
                                 customEventNativeListener: CustomEventNativeListener?,
                                 serverParameter: String?,
                                 nativeMediationAdRequest: NativeMediationAdRequest?,
                                 extras: Bundle?) {
        serverParameter?.split(",").let {
            if (it?.size != 2) {
                Log.e(TAG, "AdMob serverParameter size not 2 for Trek")
                return
            }
        }

        val placeName = serverParameter?.split(",").let {
            it?.get(0)
        } ?: run {
            Log.e(TAG, "AdMob not set place_name for Trek")
            return
        }

        val adType = serverParameter?.split(",").let {
            it?.get(1)
        } ?: run {
            Log.e(TAG, "AdMob not set [1] for Trek")
            return
        }

        if (adType != AD_TYPE_NATIVE && adType != AD_TYPE_SUPR) {
            Log.e(TAG, "AdMob not set adType for Trek")
            return
        }

        tkAdN = extras.let { it?.getString(AD_CATEGORY) }?.let {
            TKAdN(context, placeName, it, adType)
        } ?: run {
            TKAdN(context, placeName, adType)
        }

        val trekNativeAdLoader = TrekNativeAdLoader(tkAdN)
        customEventNativeListener?.let {
            trekNativeAdLoader.listener = TrekNativeCustomNativeEventForwarder(tkAdN, it)
            trekNativeAdLoader.fetchAd()
        }

    }
}