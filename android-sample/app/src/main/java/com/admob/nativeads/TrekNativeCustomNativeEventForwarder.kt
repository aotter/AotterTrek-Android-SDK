package com.admob.nativeads

import com.aotter.net.trek.ads.TKAdN
import com.aotter.net.trek.ads.TKError
import com.aotter.net.trek.model.TKAdNative
import com.google.android.gms.ads.mediation.customevent.CustomEventNativeListener

class TrekNativeCustomNativeEventForwarder(val tkAdN: TKAdN, private val listener: CustomEventNativeListener) : TrekNativeAdListener() {

    protected val TAG = TrekNativeCustomNativeEventForwarder::class.java.simpleName

    override fun onNativeAdFetched(ad: TKAdNative) {
        val mapper = TrekUnifiedNativeAdMapper(tkAdN, ad)
        listener.onAdLoaded(mapper)
    }

    override fun onAdFetchFailed(code: TKError) {
    }
}