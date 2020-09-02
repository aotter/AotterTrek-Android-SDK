package com.aotter.net.treksampleapp;

import android.content.Intent;
import android.os.Bundle;

import com.aotter.net.trek.model.TKAdNative;
import com.aotter.net.treksampleapp.activity.NativeAdMobViewActivity;
import com.aotter.net.treksampleapp.activity.NativeListViewActivity;
import com.aotter.net.treksampleapp.activity.NativeMoPubListViewActivity;
import com.aotter.net.treksampleapp.activity.NativeRCViewActivity;
import com.aotter.net.treksampleapp.activity.SuprAdListViewActivity;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.native_listview)
    public void goNativeList() {
        Intent intent = new Intent();
        intent.setClass(this, NativeListViewActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.native_rcview)
    public void goNativeRCList() {
        Intent intent = new Intent();
        intent.setClass(this, NativeRCViewActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.suprad_listview)
    public void goSuprAdList() {
        Intent intent = new Intent();
        intent.setClass(this, SuprAdListViewActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.native_mopub_listview)
    public void goMopubNativeList() {
        Intent intent = new Intent();
        intent.setClass(this, NativeMoPubListViewActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.native_admob_adview)
    public void goAdMobView() {
        Intent intent = new Intent();
        intent.setClass(this, NativeAdMobViewActivity.class);

        startActivity(intent);
    }
}
