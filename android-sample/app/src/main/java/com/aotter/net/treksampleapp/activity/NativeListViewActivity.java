package com.aotter.net.treksampleapp.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.aotter.net.gson.Gson;
import com.aotter.net.trek.ads.TKAdN;
import com.aotter.net.trek.ads.interfaces.AdListener;
import com.aotter.net.trek.model.NativeAd;
import com.aotter.net.trek.util.TrekLog;
import com.aotter.net.treksampleapp.R;
import com.aotter.net.treksampleapp.adapter.NativeListItemAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class NativeListViewActivity extends AppCompatActivity {

    @BindView(R.id.listview)
    ListView mListView;

    private Context mContext;
    private NativeListItemAdapter mAdapter;
    private TKAdN tkAdN;

    private String[] postTitle = {"幸運調色盤：12星座明天穿什麼？（6/6-6/12）",
            "遇到心上人就變色！？超夢幻「鮮花果凍唇膏」美到用了會心疼",
            "春夏流浪風情，讓你的波希米亞風格再晉級",
            "美女主持人安妮出門前快速整理秀髮小心機",
            "在海灘也能引人注目☆有點與眾不同的海灘小物特集♪",
            "H&M夏季裙裝盡顯浪漫嫵媚氣息 展現夏日隨意風～"
    };

    private String[] postImage = {"http://pnn.aotter.net/Media/show/d8404d54-aab7-4729-8e85-64fb6b92a84e.jpg",
            "http://pnn.aotter.net/Media/show/3b9bec45-33ab-4548-b192-01959f4e9571.jpg",
            "http://pnn.aotter.net/Media/show/9a9589fe-353d-4148-aa3b-40100572575b.jpg",
            "http://pnn.aotter.net/Media/show/b87f68b6-8add-428f-b119-52e49c4e68b4.jpg",
            "http://pnn.aotter.net/Media/show/5855291e-343d-4478-a760-f5c4699107d8.jpg",
            "http://pnn.aotter.net/Media/show/2b588866-47b3-4344-bb38-c9e7194af576.jpg"
    };

    private List<Object> mPostTitleList;
    private List<Object> mPostImageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_list_view);
        ButterKnife.bind(this);
        mContext = this;

        //init List
        mPostTitleList = new ArrayList<>();
        mPostImageList = new ArrayList<>();

        for (String title : postTitle) {
            mPostTitleList.add(title);
        }

        for (String image : postImage) {
            mPostImageList.add(image);
        }

        mAdapter = new NativeListItemAdapter(mContext, mPostTitleList, mPostImageList);
        mListView.setAdapter(mAdapter);
        /**
         "post_third" is custom payload place name
         YOUR_CATEGORY_NAME is custom category name
         tkAdN = new TKAdN(this, "post_third"); if don't have category, you can without it.
         **/
        tkAdN = new TKAdN(this, "post_third", "NATIVE");
        tkAdN.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded(NativeAd nativeAd) {
                if (nativeAd != null && mAdapter != null) {
                    mAdapter.addNativeAd(tkAdN, nativeAd);
                }
            }

            @Override
            public void onAdFail() {
                Log.d("NativeListViewActivity", "No Ad");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (tkAdN != null)
            tkAdN.resume();
    }
}
