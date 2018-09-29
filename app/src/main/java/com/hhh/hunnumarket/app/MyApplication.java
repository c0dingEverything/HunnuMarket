package com.hhh.hunnumarket.app;

import android.app.Application;

import com.hhh.hunnumarket.utils.DataUtil;
import com.hhh.hunnumarket.utils.SharedPreferenceUtil;

import org.xutils.x;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        SharedPreferenceUtil.init(this);
        DataUtil.init(this);
    }
}
