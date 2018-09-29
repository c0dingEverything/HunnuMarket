package com.hhh.hunnumarket.utils;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hhh.hunnumarket.bean.Category;
import com.hhh.hunnumarket.bean.ResponseObject;
import com.hhh.hunnumarket.bean.User;
import com.hhh.hunnumarket.consts.Api;
import com.hhh.hunnumarket.dbhelper.CategoryDao;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

public class DataUtil {
    private static List<Category> categoryList;
    private static User mUser;
    private static Application mApp;

    public static void init(Application app) {
        mApp = app;
    }


    public static void updateUser() {
        mUser = null;
    }

    public static User getUerInfo() {
        if (mUser == null) {
            mUser = SharedPreferenceUtil.getUser();
        }
        return mUser;
    }

    public static void updateUserInfoFromServer() {
        RequestParams params = new RequestParams(Api.GET_USER_INFO);
        params.addBodyParameter("uid", SharedPreferenceUtil.getUid() + "");
        params.addBodyParameter("access_token", SharedPreferenceUtil.getAccessToken());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ResponseObject<User> responseObject = new Gson().fromJson(result, new TypeToken<ResponseObject<User>>() {
                }.getType());
                if (responseObject.getStatus() == ResponseObject.STATUS_OK) {
                    mUser = responseObject.getData();
                    SharedPreferenceUtil.updateUserInfo(mUser);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public static List<Category> getCategories() {
        if (categoryList != null && categoryList.size() > 0) {
            return categoryList;
        } else {
            CategoryDao categoryDao = new CategoryDao(mApp.getApplicationContext());
            categoryList = categoryDao.query();
            return categoryList;
        }
    }

    private static void saveCategoriesToDatabase() {
        if (categoryList != null && categoryList.size() > 0) {
            CategoryDao categoryDao = new CategoryDao(mApp.getApplicationContext());
            categoryDao.insert(categoryList);
        }
    }

    public static void updateCategoriesFromServer() {
        RequestParams params = new RequestParams(Api.GET_CATEGORIES);
//        params.addBodyParameter("uid", SharedPreferenceUtil.getUid(context) + "");
//        params.addBodyParameter("access_token", SharedPreferenceUtil.getAccessToken(context));
        try {
            x.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    ResponseObject<List<Category>> responseObject = new Gson().fromJson(result, new TypeToken<ResponseObject<List<Category>>>() {
                    }.getType());
                    if (responseObject.getStatus() == ResponseObject.STATUS_OK) {
                        categoryList = responseObject.getData();
                        saveCategoriesToDatabase();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {

                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });


        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

}
