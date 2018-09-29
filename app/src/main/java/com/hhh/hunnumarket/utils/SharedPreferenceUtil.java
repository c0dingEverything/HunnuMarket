package com.hhh.hunnumarket.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.hhh.hunnumarket.R;
import com.hhh.hunnumarket.bean.User;
import com.hhh.hunnumarket.bean.UserToken;

import java.util.Map;

public class SharedPreferenceUtil {
    public final static String USER_INFO = "user_info";
    public final static String USER_TOKEN = "user_token";
    private static int uid;
    private static String access_token;
    private static Application mApp;
    private static SharedPreferences sp;

    public static void init(Application app) {
        mApp = app;
    }

    public static void updateUserInfo(User user) {
        SharedPreferences.Editor edit = mApp.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE).edit();
        edit.putString("nickname", user.getNickname());
        edit.putString("qq", user.getQq());
        edit.putString("phone", user.getPhone_number());
        edit.putString("location", user.getUser_location());
        edit.putString("head_url", user.getImage_url());
        edit.apply();
        DataUtil.updateUser();
    }

    public static User getUser() {
        SharedPreferences sp = mApp.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        User user = new User();
        user.setSid(sp.getString("sid", ""));
        user.setNickname(sp.getString("nickname", mApp.getString(R.string.nickname)));
        user.setPhone_number(sp.getString("phone", mApp.getString(R.string.phone_number)));
        user.setQq(sp.getString("qq", mApp.getString(R.string.qq)));
        user.setImage_url(sp.getString("head_url", ""));
        user.setUser_location(sp.getString("location", ""));
        return user;
    }


    public static String getAccessToken() {
        if (TextUtils.isEmpty(access_token)) {
            access_token = mApp.getSharedPreferences(USER_TOKEN, Context.MODE_PRIVATE).getString("access_token", "");
            return access_token;
        } else {
            return access_token;
        }
    }

    public static void updateAccessToken() {
        access_token = mApp.getSharedPreferences(USER_TOKEN, Context.MODE_PRIVATE).getString("access_token", "");
    }


    public static int getUid() {
        if (uid == 0) {
            uid = mApp.getSharedPreferences(USER_TOKEN, Context.MODE_PRIVATE).getInt("uid", 0);
            return uid;
        } else {
            return uid;
        }
    }

    public static void saveUserToken(UserToken userToken) {
        SharedPreferences sp = mApp.getSharedPreferences(USER_TOKEN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("access_token", userToken.getAccess_token());
        editor.putString("expiry", userToken.getExpiry());
        editor.putInt("uid", userToken.getUid());
        editor.apply();
    }

    public static UserToken getUserToken() {
        SharedPreferences sp = mApp.getSharedPreferences(USER_TOKEN, Context.MODE_PRIVATE);
        UserToken userToken = new UserToken();
        userToken.setUid(sp.getInt("uid", -1));
        userToken.setAccess_token(sp.getString("access_token", ""));
        userToken.setExpiry(sp.getString("expiry", "0"));
        return userToken;
    }


    /**
     * 保存信息到本地
     *
     * @param sid
     * @param pwd
     */
    public static void saveUserInfo(boolean checked, String sid, String pwd) {
        SharedPreferences.Editor editor = getEditor(USER_INFO);
        editor.putString("sid", sid);
        editor.putString("pwd", pwd);
        editor.putBoolean("checked", checked);
        editor.apply();
    }

    /**
     * @param which   指定要打开的sp, 为空就默认user_info
     * @param checked
     */

    public static void setCheckBoxStatus(String which, boolean checked) {
        SharedPreferences.Editor editor = getEditor(which);
        editor.putBoolean("checked", checked);
        editor.apply();
    }

    private static SharedPreferences.Editor getEditor(String which) {
        if (which == null) {
            which = USER_INFO;
        }
        SharedPreferences sp = mApp.getSharedPreferences(which, Context.MODE_PRIVATE);
        return sp.edit();
    }


    public static Map<String, ?> getUserInfo() {
        SharedPreferences sp = mApp.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        return sp.getAll();
    }

    public static Object getUserInfo(String target) {
        SharedPreferences sp = mApp.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        return sp.getAll().get(target);
    }

}
