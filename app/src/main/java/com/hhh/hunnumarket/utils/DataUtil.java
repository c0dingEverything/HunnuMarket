package com.hhh.hunnumarket.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hhh.hunnumarket.bean.Category;
import com.hhh.hunnumarket.bean.ResponseObject;
import com.hhh.hunnumarket.consts.Api;
import com.hhh.hunnumarket.dbhelper.CategoryDao;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

public class DataUtil {
    private static List<Category> categoryList;



    public static List<Category> getCategories(Context context) {
        if (categoryList != null && categoryList.size() > 0) {
            return categoryList;
        } else {
            CategoryDao categoryDao = new CategoryDao(context);
            categoryList = categoryDao.query();
            return categoryList;
        }
    }

    private static void saveCategoriesToDatabase(Context context) {
        if (categoryList != null && categoryList.size() > 0) {
            CategoryDao categoryDao = new CategoryDao(context);
            categoryDao.insert(categoryList);
        }
    }

    public static void getCategoriesFromServer(final Context context) {
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
                        saveCategoriesToDatabase(context);
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
