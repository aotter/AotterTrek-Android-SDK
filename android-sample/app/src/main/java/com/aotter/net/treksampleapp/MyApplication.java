package com.aotter.net.treksampleapp;

import android.app.Application;

import com.aotter.net.trek.AotterTrek;

/**
 * Created by SmallMouth on 2016/6/13.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // init AotterTrek
        AotterTrek.init(this, "DNgNhOwfbUkOqcQFI+uD", "1k+sYKMLZrclCRmgw/esYNZbjAhArT7Vn42cxfn3f/tgmT0XJZI4mNiNwBYLu9GOet7YtiT6");
        AotterTrek.setEmail("XXXXXXXX@gmail.com")
                .setFbId("123455566");
    }

}
