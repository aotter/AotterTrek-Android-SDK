package com.admob.nativeads

import com.aotter.net.trek.ads.TKError
import com.aotter.net.trek.model.TKAdNative

abstract class TrekNativeAdListener {
    /**
     * Called when a native ad is successfully fetched.
     */
    open fun onNativeAdFetched(ad: TKAdNative) {
        // Default is to do nothing.
    }

    /**
     * Called when an ad fetch fails.
     *
     * @param code The reason the fetch failed.
     */
    open fun onAdFetchFailed(code: TKError) {
        // Default is to do nothing.
    }
}
