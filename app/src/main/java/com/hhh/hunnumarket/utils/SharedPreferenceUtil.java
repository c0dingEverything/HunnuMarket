package com.hhh.hunnumarket.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.hhh.hunnumarket.bean.UserToken;

import java.util.Map;

public class SharedPreferenceUtil {
    public final static String USER_INFO = "user_info";
    public final static String USER_TOKEN = "user_token";
    private static int uid;
    private static String access_token;

    public static String getAccessToken(Context context) {
        if (TextUtils.isEmpty(access_token)) {
            access_token = context.getSharedPreferences(USER_TOKEN, Context.MODE_PRIVATE).getString("access_token", "");
            return access_token;
        } else {
            return access_token;
        }
    }


    public static int getUid(Context context) {
        if (uid == 0) {
            uid = context.getSharedPreferences(USER_TOKEN, Context.MODE_PRIVATE).getInt("uid", 0);
            return uid;
        } else {
            return uid;
        }
    }

    public static void saveUserToken(Context context, UserToken userToken) {
        SharedPreferences sp = context.getSharedPreferences(USER_TOKEN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("access_token", userToken.getAccess_token());
        editor.putString("expiry", userToken.getExpiry());
        editor.putInt("uid", userToken.getUid());
        editor.apply();
    }

    public static UserToken getUserToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences(USER_TOKEN, Context.MODE_PRIVATE);
        UserToken userToken = new UserToken();
        userToken.setUid(sp.getInt("uid", -1));
        userToken.setAccess_token(sp.getString("access_token", ""));
        userToken.setExpiry(sp.getString("expiry", "0"));
        return userToken;
    }


    /**
     * 保存信息到本地
     *
     * @param context
     * @param sid
     * @param pwd
     */
    public static void saveUserInfo(Context context, boolean checked, String sid, String pwd) {
        SharedPreferences.Editor editor = getEditor(context, USER_INFO);
        editor.putString("sid", sid);
        editor.putString("pwd", pwd);
        editor.putBoolean("checked", checked);
        editor.apply();
    }

    /**
     * @param context
     * @param which   指定要打开的sp, 为空就默认user_info
     * @param checked
     */

    public static void setCheckBoxStatus(Context context, String which, boolean checked) {

        SharedPreferences.Editor editor = getEditor(context, which);
        editor.putBoolean("checked", checked);
        editor.apply();
    }

    private static SharedPreferences.Editor getEditor(Context context, String which) {
        if (which == null) {
            which = USER_INFO;
        }
        SharedPreferences sp = context.getSharedPreferences(which, Context.MODE_PRIVATE);
        return sp.edit();
    }


    public static Map<String, ?> getUserInfo(Context context) {
        SharedPreferences sp = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        return sp.getAll();
    }

    public static Object getUserInfo(Context context, String target) {
        SharedPreferences sp = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        return sp.getAll().get(target);
    }

}
