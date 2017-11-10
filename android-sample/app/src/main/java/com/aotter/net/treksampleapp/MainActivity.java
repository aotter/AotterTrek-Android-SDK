package com.aotter.net.treksampleapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aotter.net.trek.ads.AdError;
import com.aotter.net.trek.ads.TrekAd;
import com.aotter.net.trek.ads.interfaces.InstreamVideoListener;
import com.aotter.net.treksampleapp.activity.InterActiveListViewActivity;
import com.aotter.net.treksampleapp.activity.NativeListViewActivity;
import com.aotter.net.treksampleapp.activity.VideoListViewActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private TrekAd trekAd;
    boolean FLAG_VIDEO = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        trekAd = new TrekAd(this);
        trekAd.loadInstreamVideo();
        trekAd.setCloseButtonDelayTime(5000);
        //可以設定關閉按鈕的延遲秒數,預設為0

        trekAd.setInstreamVideoListener(new InstreamVideoListener() {
            @Override
            public void onInstreamVideoLoadSuccess() {
                //影片讀取成功
                FLAG_VIDEO = true;
            }

            @Override
            public void onInstreamVideoLoadFailure(@NonNull AdError errorCode) {
                //影片讀取錯誤
            }

            @Override
            public void onInstreamVideoStarted() {
                //影片開始播放
            }

            @Override
            public void onInstreamVideoPlaybackError() {
                //播放錯誤
            }


            @Override
            public void onInstreamVideoClosed() {
                //按下關閉按鈕
            }

            @Override
            public void onInstreamVideoCompleted() {
                //影片播放完畢
            }
        });
    }

    @OnClick(R.id.native_listview)
    public void goNativeList() {
        Intent intent = new Intent();
        intent.setClass(this, NativeListViewActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.video_listview)
    public void goVideoList() {
        Intent intent = new Intent();
        intent.setClass(this, VideoListViewActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.instream_video)
    public void goInstream_List() {
        if (FLAG_VIDEO)
            trekAd.showInstreamVideo();
    }

    @OnClick(R.id.native_interactive)
    public void goNativeInterActive() {
        Intent intent = new Intent();
        intent.setClass(this, InterActiveListViewActivity.class);
        startActivity(intent);
    }
}
