package com.aotter.net.treksampleapp.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.aotter.net.trek.ads.TKAdN;
import com.aotter.net.treksampleapp.R;
import com.aotter.net.treksampleapp.adapter.NativeListItemAdapter;
import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.common.logging.MoPubLog;
import com.mopub.nativeads.MoPubAdAdapter;
import com.mopub.nativeads.MoPubNativeAdPositioning;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.RequestParameters;
import com.mopub.nativeads.TrekAdRenderer;
import com.mopub.nativeads.TrekMoPubAdAdapter;
import com.mopub.nativeads.TrekNativeAdSource;
import com.mopub.nativeads.ViewBinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NativeMoPubListViewActivity extends AppCompatActivity {

    @BindView(R.id.listview)
    ListView mListView;

    private NativeListItemAdapter mAdapter;
    private TKAdN tkAdN;
    private MoPubAdAdapter mAdAdapter;
    private TrekAdRenderer trekAdRenderer;
    private RequestParameters mRequestParameters;

    private String[] postTitle = {"幸運調色盤：12星座明天穿什麼？（6/6-6/12）",
            "遇到心上人就變色！？超夢幻「鮮花果凍唇膏」美到用了會心疼",
            "春夏流浪風情，讓你的波希米亞風格再晉級",
            "美女主持人安妮出門前快速整理秀髮小心機",
            "在海灘也能引人注目☆有點與眾不同的海灘小物特集♪",
            "H&M夏季裙裝盡顯浪漫嫵媚氣息 展現夏日隨意風～"
    };

    private String[] postImage = {"https://pnn.aotter.net/Media/show/d8404d54-aab7-4729-8e85-64fb6b92a84e.jpg",
            "https://pnn.aotter.net/Media/show/3b9bec45-33ab-4548-b192-01959f4e9571.jpg",
            "https://pnn.aotter.net/Media/show/9a9589fe-353d-4148-aa3b-40100572575b.jpg",
            "https://pnn.aotter.net/Media/show/b87f68b6-8add-428f-b119-52e49c4e68b4.jpg",
            "https://pnn.aotter.net/Media/show/5855291e-343d-4478-a760-f5c4699107d8.jpg",
            "https://pnn.aotter.net/Media/show/2b588866-47b3-4344-bb38-c9e7194af576.jpg"
    };

    private SdkInitializationListener initSdkListener() {
        return () -> {

            Log.d("initSdkListener", "initSdkListener: ");
       /* MoPub SDK initialized.
       Check if you should show the consent dialog here, and make your ad requests. */
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_list_view);
        ButterKnife.bind(this);

        //init List
        List<Object> mPostTitleList = new ArrayList<>();
        List<Object> mPostImageList = new ArrayList<>();

        Collections.addAll(mPostTitleList, postTitle);

        Collections.addAll(mPostImageList, postImage);


        SdkConfiguration sdkConfiguration = new SdkConfiguration.Builder("c23fe2eaea7c4522af307e1bb6aa7eda")
                .withLogLevel(MoPubLog.LogLevel.DEBUG)
                .withLegitimateInterestAllowed(false)
                .build();

        MoPub.initializeSdk(this, sdkConfiguration, initSdkListener());


        mAdapter = new NativeListItemAdapter(this, mPostTitleList, mPostImageList);

        mAdAdapter = new TrekMoPubAdAdapter(this, mAdapter, new MoPubNativeAdPositioning.MoPubServerPositioning(), new TrekNativeAdSource());

        // Set up a renderer that knows how to put ad data in your custom native view.
        final MoPubStaticNativeAdRenderer staticAdRender = new MoPubStaticNativeAdRenderer(
                new ViewBinder.Builder(R.layout.ad_item)
                        .titleId(R.id.ad_title)
                        .mainImageId(R.id.ad_image)
                        .textId(R.id.ad_summary)
                        .addExtra("sponsor", R.id.ad_publisher)
                        .build());

        // Trek Audience Network
        trekAdRenderer = new TrekAdRenderer(this, false,
                new ViewBinder.Builder(R.layout.ad_item)
                        .titleId(R.id.ad_title)
                        .mainImageId(R.id.ad_image)
                        .textId(R.id.ad_summary)
                        .addExtra("sponsor", R.id.ad_publisher)
                        .build());

        //Specify which native assets you want to use in your ad.
        EnumSet<RequestParameters.NativeAdAsset> assetsSet = EnumSet.of(RequestParameters.NativeAdAsset.TITLE, RequestParameters.NativeAdAsset.TEXT,
                RequestParameters.NativeAdAsset.CALL_TO_ACTION_TEXT, RequestParameters.NativeAdAsset.MAIN_IMAGE,
                RequestParameters.NativeAdAsset.ICON_IMAGE, RequestParameters.NativeAdAsset.STAR_RATING);
        mAdAdapter.registerAdRenderer(staticAdRender);
        mAdAdapter.registerAdRenderer(trekAdRenderer);

        mListView.setAdapter(mAdAdapter);

        // Set up your request parameters
        mRequestParameters = new RequestParameters.Builder()
                .keywords("my targeting keywords")
                .desiredAssets(assetsSet)
                .build();

        mAdAdapter.loadAds("c23fe2eaea7c4522af307e1bb6aa7eda", mRequestParameters);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (tkAdN != null)
            tkAdN.resume();
    }
}
