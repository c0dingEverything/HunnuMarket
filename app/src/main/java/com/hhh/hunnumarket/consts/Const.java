package com.hhh.hunnumarket.consts;

import android.content.Context;

import com.hhh.hunnumarket.R;

public class Const {
    public static String[] getLocations(Context context){
        return new String[]{context.getString(R.string.tianma),context.getString(R.string.dezhi),context.getString(R.string.changtangshan),context.getString(R.string.jiangbian),context.getString(R.string.nanyuan)};
    }
}
