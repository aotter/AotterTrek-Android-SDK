package com.admob.nativeads

import android.graphics.drawable.Drawable
import android.net.Uri
import com.google.android.gms.ads.formats.NativeAd

class TrekNativeMappedImage(private val mUri: Uri,private val mScale: Double?) : NativeAd.Image() {

    private var mDrawable: Drawable? = null


    override fun getDrawable(): Drawable? {
        return mDrawable
    }

    override fun getUri(): Uri? {
        return mUri
    }

    override fun getScale(): Double {
        mScale?.let {
            return it
        }
        // Default scale is 1.
        return 1.0
    }
}