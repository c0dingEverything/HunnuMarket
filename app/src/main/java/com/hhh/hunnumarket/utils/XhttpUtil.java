package com.hhh.hunnumarket.utils;

import org.xutils.http.RequestParams;

public class XhttpUtil {
    public static RequestParams WrapParamsWithToken(RequestParams params) {
        params.addBodyParameter("uid", String.valueOf(SharedPreferenceUtil.getUid()));
        params.addBodyParameter("access_token", SharedPreferenceUtil.getAccessToken());
        return params;
    }
}
